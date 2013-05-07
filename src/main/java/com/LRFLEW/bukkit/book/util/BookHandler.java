package com.LRFLEW.bukkit.book.util;

import com.LRFLEW.bukkit.book.BMPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.logging.Level;

public class BookHandler {
    private FileConfiguration bookLog = null;
    private File bookFile = null;

    public void reloadBookLog() {
        if(!configExists()){
            if(bookFile == null) bookFile = new File(BMPlugin.getPlugin().getDataFolder(), "books.yml");
            bookLog = YamlConfiguration.loadConfiguration(BMPlugin.getPlugin().getResource("books.yml"));
            saveBookLog();
        }
        if(bookFile == null) bookFile = new File(BMPlugin.getPlugin().getDataFolder(), "books.yml");
        bookLog = YamlConfiguration.loadConfiguration(bookFile);
        InputStream defaultBookStream = BMPlugin.getPlugin().getResource("books.yml");
        if(defaultBookStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultBookStream);
            bookLog.setDefaults(defaultConfig);
        }
    }

    private FileConfiguration getBookLog() {
        if(bookLog == null) this.reloadBookLog();
        return bookLog;
    }

    public void saveBookLog() {
        if(bookLog == null || bookFile == null) return;
        try {
            getBookLog().save(bookFile);
        } catch(IOException e) {
            BMPlugin.getPlugin().getLogger().log(Level.SEVERE, "Could not save books to " + bookLog, e);
        }
    }

    private boolean configExists() {
        return new File(BMPlugin.getPlugin().getDataFolder(), "books.yml").exists();
    }

    private boolean bookExists(String title){
        return getBookLog().get(title.toLowerCase()) != null;
    }

    public String getBooks(){
        Set<String> archive = bookLog.getKeys(false);
        if(archive == null || archive.isEmpty()) return "There are no saved books.";
        String books = "";
        for(String book : archive){
            if(books.length() > 1) books = books + ", ";
            books = books + book;
        }
        return books.toLowerCase();
    }

    public ItemStack getBook(String title){
        title = title.replaceAll(" ", "-").toLowerCase();
        if(!bookExists(title)) return null;
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta book = (BookMeta) is.getItemMeta();
        book.setTitle(bookLog.getString(title + ".title"));
        book.setAuthor(bookLog.getString(title + ".author"));
        book.setPages(bookLog.getStringList(title + ".content"));
        is.setItemMeta(book);
        return is;
    }

    public void saveBook(String title, BookMeta book){
        title = title.replaceAll(" ", "-").toLowerCase();
        bookLog.set(title + ".title", book.getTitle());
        bookLog.set(title + ".author", book.getAuthor());
        bookLog.set(title + ".pages", book.getPageCount());
        bookLog.set(title + ".content", book.getPages());
        saveBookLog();
    }

    public boolean deleteBook(String title){
        title = title.replaceAll(" ", "-").toLowerCase();
        bookLog.set(title, null);
        saveBookLog();
        return bookExists(title);
    }
}