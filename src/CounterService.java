import model.CalculateResult;
import notification.Observer;

public class CounterService implements Observer<CalculateResult> {

    private int successChecks = 0;

    public int getSuccessChecks() {
        return successChecks;
    }

    @Override
    public void update(CalculateResult result) {
        if (result.found) {
            successChecks++;
        }
    }

}
