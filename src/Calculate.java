import model.CalculateResult;
import model.DownloadResult;

import java.util.Random;

public class Calculate {
    public CalculateResult calculate(DownloadResult download) {
        Random r = new Random();

        CalculateResult result = new CalculateResult();
        result.id = download.id;
        for (int i = 0; i < 200000; i++) {
            int check = r.nextInt(100000);
            if (download.check(check)) {
                result.found = true;
                return result;
            }
        }
        return result;
    }
}
