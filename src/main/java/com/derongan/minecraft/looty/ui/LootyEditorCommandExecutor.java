package com.derongan.minecraft.looty.ui;

import com.derongan.minecraft.looty.LootyPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class LootyEditorCommandExecutor implements CommandExecutor {

    private final LootyPlugin plugin;
    private final LootyEditorFactory lootyEditorFactory;

    private GUIHolder guiHolder;

    @Inject
    public LootyEditorCommandExecutor(LootyPlugin plugin, LootyEditorFactory lootyEditorFactory) {
        this.plugin = plugin;
        this.lootyEditorFactory = lootyEditorFactory;

        guiHolder = new GUIHolder(6, "Looty Editor", lootyEditorFactory.buildEditor(), plugin);
    }
//
//    private Layout getTriggerEditorScene() {
//        Layout.Builder builder = Layout.builder()
//                .addElement(0, 0, Element.forMaterial(Material.LEVER, "Triggers"))
//                .addElement(0, 1, Element.forMaterial(Material.LEATHER_BOOTS, "Modifiers"));
//
//        Iterator<Material> materialIterator = Iterables.cycle(Arrays.stream(Material.values())
//                .filter(mat -> mat.name().endsWith("WOOL"))
//                .collect(toImmutableList())).iterator();
//
//        FillableElement fillableElement = new FillableElement(3, 3);
//
//
//        int x = 1;
//        for (SkillTrigger.Trigger value : SkillTrigger.Trigger.values()) {
//            if (value != SkillTrigger.Trigger.UNRECOGNIZED) {
//                Element element = Element.forMaterial(materialIterator.next(), value.name());
//                ClickableElement clickableElement = new ClickableElement(element, () -> {
//                    fillableElement.addElement(element);
//                });
//                builder.addElement(x++, 0, clickableElement);
//            }
//        }
//
//        x = 1;
//        for (SkillTrigger.Modifier value : SkillTrigger.Modifier.values()) {
//            if (value != SkillTrigger.Modifier.UNRECOGNIZED && value != SkillTrigger.Modifier.UNKNOWN) {
//                Element element = Element.forMaterial(materialIterator.next(), value.name());
//                ClickableElement clickableElement = new ClickableElement(element, () -> {
//                    fillableElement.addElement(element);
//                });
//                builder.addElement(x++, 1, clickableElement);
//            }
//        }


    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            guiHolder.show(player);
        }

        return true;
    }
}
