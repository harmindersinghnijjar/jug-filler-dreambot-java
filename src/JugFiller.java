import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import static org.dreambot.api.methods.interactive.Players.getLocal;
import static org.dreambot.api.methods.walking.impl.Walking.walk;

@ScriptManifest(author = "Harminder Singh Nijjar", category = Category.MONEYMAKING, name = "Jug Filler", version = 0.1)
public class JugFiller extends AbstractScript {

    private Area fountainArea = new Area(2946, 3369, 2949, 3366);
    private Area bankArea = new Area(2946, 3369, 2949, 3366);

    @Override
    public void onStart() {
        log("Welcome to Jug Filler!");
    }

    @Override
    public int onLoop() {
        if (Inventory.isFull()) {
            if (bankArea.contains(getLocal())) {
                depositAll();
            } else {
                walk(bankArea.getRandomTile());
            }
        } else {
            if (fountainArea.contains(getLocal())) {
                fillJugs();
            } else {
                walk(fountainArea.getRandomTile());
            }
        }
        return Calculations.random(500, 1000);
    }

    private void fillJugs() {
        GameObject fountain = GameObjects.closest("Fountain");
        if (fountain != null) {
            if (fountain.interact("Fill")) {
                Sleep.sleepUntil(() -> Inventory.isFull(), Calculations.random(5000, 10000));
            }
        }
    }

    private void depositAll() {
        NPC banker = NPCs.closest("Banker");
        if (banker != null) {
            if (banker.interact("Bank")) {
                Sleep.sleepUntil(() -> Bank.isOpen(), Calculations.random(5000, 10000));
                if (Bank.isOpen()) {
                    Bank.depositAllItems();
                    Sleep.sleepUntil(() -> Inventory.isEmpty(), Calculations.random(5000, 10000));
                    Bank.close();
                }
            }
        }
    }
}
