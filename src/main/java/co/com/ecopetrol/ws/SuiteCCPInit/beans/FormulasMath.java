package co.com.ecopetrol.ws.SuiteCCPInit.beans;

/**
 *
 * @author STC
 */
public abstract class FormulasMath {

    public static Double getVolCBE(Double nivel) {
        return (0.0056 * (Math.pow(nivel, 2))) + (5.579 * nivel) + 97.46;
    }

}
