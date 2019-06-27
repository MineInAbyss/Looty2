package com.derongan.minecraft.looty.config.serialization;

public class ItemDumperTest {
//    private Map<?, ?> blazeReapMap;
//
//    private Yaml yaml;
//    private ItemDumper itemDumper;
//
//    @Before
//    public void setUp() {
//        Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new ProtoJsonSerializer())
//                .registerTypeAdapter(ActionEntityBuilder.class, new ActionEntityBuilderSerializer())
//                .registerTypeAdapter(Skill.class, new SkillSerializer())
//                .registerTypeAdapter(ItemType.class, new ItemTypeSerializer())
//                .create();
//        DumperOptions dumperOptions = new DumperOptions();
//
//        yaml = new Yaml();
//
//        blazeReapMap = this.yaml.load(TestItems.BLAZE_REAP_YAML);
//
//        itemDumper = new ItemDumper(gson, yaml);
//    }
//
//    @Test
//    public void testDump() {
//        ActionEntityBuilder actionEntityBuilder = new ActionEntityBuilder();
//        actionEntityBuilder.addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(3).build()))
//                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
//                        .setFillStyle(ParticleInfo.FillStyle.TARGET)
//                        .setParticleName(org.bukkit.Particle.EXPLOSION_HUGE.name())
//                        .build()))
//                .addComponent(() -> new TargetChooser(TargetChooserInfo.newBuilder()
//                        .setOffset(Offset.newBuilder()
//                                .setDirectionType(DirectionType.HEADING)
//                                .setLocationReferenceType(LocationReferenceType.IMPACT)
//                                .build())
//                        .build()))
//                .addComponent(() -> new Damage(DamageInfo.newBuilder().setDamage(5).build()));
//        Skill skill = Skill.builder().addActionBuilder(actionEntityBuilder).build();
//        ItemType simpleItem = ItemType.builder()
//                .setMaterial(Material.DIAMOND_PICKAXE)
//                .setName("Blaze Reap")
//                .setDurability((short) 1)
//                .setItemRarity(ItemRarity.SPECIAL_GRADE)
//                .setLore(ImmutableList.of("Hello world"))
//                .addSkillWithTrigger(SkillTrigger.builder().setHand(Hand.LEFT).build(), skill)
//                .build();
//
//
//        String actualYaml = itemDumper.toConfig(simpleItem);
//        Map<?, ?> actualResults = this.yaml.load(actualYaml);
//        assertThat(actualResults).isEqualTo(blazeReapMap);
//    }
//
//    @Test
//    @Ignore
//    public void dumpToConsole() {
//        Looty looty = new Looty(null, null, null, null, null, null, null, null);
//
//        System.out.println(itemDumper.toConfig(looty.rainStick()));
//        System.out.println(itemDumper.toConfig(looty.blazeReap()));
//    }
}