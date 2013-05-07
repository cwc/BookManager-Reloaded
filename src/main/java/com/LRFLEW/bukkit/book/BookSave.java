package com.LRFLEW.bukkit.book;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookSave {
	
	public static void saveBook (BookMeta book, CommandSender sender, String name) {
        BMPlugin.getPlugin().bookHandler.saveBook(name, book);
        if(BMPlugin.getPlugin().bookHandler.getBook(name) != null)
            sender.sendMessage(ChatColor.GREEN + "Your book has been saved.");
        else
            sender.sendMessage(ChatColor.RED + "Your book could not be saved.");
	}
	
	public static void deleteBook (CommandSender sender, String name) {
		if(BMPlugin.getPlugin().bookHandler.getBook("name") != null) {
			sender.sendMessage("There is no saved book by that name");
			return;
		}
        BMPlugin.getPlugin().bookHandler.deleteBook(name);
		sender.sendMessage("Book deleted, unchecked at the moment!");
	}
	
	public static void loadBook (VaultHook econ, Player player, String name, boolean firstJoin) {
        ItemStack is = BMPlugin.getPlugin().bookHandler.getBook(name);
        if(is == null){
            player.sendMessage(ChatColor.RED + "There is no book saved by the name of \"" + name + "\"");
            return;
        }
        if(!firstJoin){
            BookMeta book = (BookMeta) is.getItemMeta();
            /*** Needs testing! ***/
            if(!player.hasPermission("bookmanager.loadtxt.all") && !book.getAuthor().equalsIgnoreCase(player.getName())){
                player.sendMessage(ChatColor.RED + "You are not allowed to access that book.");
                return;
            }
            /*** END ***/
            // TODO: Implement Material (true/false). Implement economy cost.
            if(!player.hasPermission("bookmanager.loadtxt.free")){
                if(BookMakeUse.useMaterials(player, 1)) return;
            }
        }
		/*
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(
				new File(folder, "conf.yml"));
		if(!firstJoin) {
            if (!yc.getBoolean("available", false) &&
                    !player.hasPermission("bookmanager.loadtxt.all")) {
                player.sendMessage("You cannot access that book, sorry :(");
                return;
            }
            if (!player.hasPermission("bookmanager.loadtxt.free")) {
                if (yc.getBoolean("mat"))
                    if (BookMakeUse.useMaterials(player, 1)) return;
                double d = yc.getDouble("cost");
                if (d > 0) econ.spendMoney(player, d);
            }
        }
        */
        player.getInventory().addItem(is);
	}
}
