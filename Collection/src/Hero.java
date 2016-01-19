/**
 * Created by JLyc.Development@gmail.com on 1/13/2016.
 */
public class Hero {
    protected int agility;
    protected int strenght;
    protected int inteligence;

    protected boolean range;

    protected long dmg;
    protected long mana;
    protected long hp;
    protected long ms;

    protected float armor;
    protected double as;

    protected String name;

    protected int[] spells;

    public Hero(String name, int agility, int strenght, int inteligence, boolean range, long dmg, long mana, long hp, long ms, float armor, double as) {
        this.agility = agility;
        this.strenght = strenght;
        this.inteligence = inteligence;
        this.range = range;
        this.dmg = dmg;
        this.mana = mana;
        this.hp = hp;
        this.ms = ms;
        this.armor = armor;
        this.as = as;
    }

    public long attack(){
        return dmg;
    }

    public void move(int positionX, int positionY){
        GameMechanizmus.movePossition(positionX, positionY);
    }

    public void lvlUP(){
        agility += 1;
        strenght += 3;
        inteligence += 2;

        hp = strenght*19;
        mana = inteligence + 13;
    }

    public Spell castSpell(int ktorySpell){
        return spells[ktorySpell];
    }
}
