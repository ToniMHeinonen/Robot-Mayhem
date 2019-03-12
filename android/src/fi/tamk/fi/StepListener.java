package fi.tamk.fi;


// Will listen to step alerts
public interface StepListener {

    public void step(long timeNs);

}
