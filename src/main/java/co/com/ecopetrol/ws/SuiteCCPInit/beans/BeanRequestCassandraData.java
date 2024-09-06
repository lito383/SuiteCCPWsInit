package co.com.ecopetrol.ws.SuiteCCPInit.beans;

/**
 *
 * @author STC
 */
public class BeanRequestCassandraData {

    private String strLstTags = null;
    private String strCalendarStart;
    private String strCalendarEnd;

    public String getStrLstTags() {
        return strLstTags;
    }

    public void setStrLstTags(String strLstTags) {
        this.strLstTags = strLstTags;
    }

    public String getStrCalendarStart() {
        return strCalendarStart;
    }

    public void setStrCalendarStart(String strCalendarStart) {
        this.strCalendarStart = strCalendarStart;
    }

    public String getStrCalendarEnd() {
        return strCalendarEnd;
    }

    public void setStrCalendarEnd(String strCalendarEnd) {
        this.strCalendarEnd = strCalendarEnd;
    }
}
