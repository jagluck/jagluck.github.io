
/**
 * Infrared Motion Sensor used with a Raspberry Pie
 * 
 * @author Jake Gluck
 * @version 0.0
 */

import java.util.*;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Sensor{

    GpioController gpio;
    GpioPinDigitalInput myPin;
    private List<MotionListener> listeners = new ArrayList<MotionListener>();
    Timer timer = new Timer();
    PinState overallState;
    boolean timerState = false;
    /**
     * Constructor for objects of class Sensor
     * @param pin The location of the gpio pin
     */
    public Sensor(Pin pin) {
        // create gpio controller
        gpio = GpioFactory.getInstance();
        // provision gpio pin #07 as an input pin with its internal pull down resistor enabled
        myPin = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
    }
    
    public void addListener(MotionListener toAdd){
        listeners.add(toAdd);
    }
    
    public void sendMotion(){
        for (MotionListener m: listeners)
            m.motionPresent();
    }
    
    public void sendNoMotion(){
        for (MotionListener m: listeners)
            m.motionAbsent();
    }
    
    public PinState getOverallState(){
        return overallState;
    }
    
    public void setOverallState(PinState state){
        this.overallState = state;
    }
    
    public boolean getTimerState(){
        return timerState;
    }
    
    public void startTimer(){
        timerState = true;
        timer.schedule( new TimerTask(){
              public void run(){
                   sendNoMotion();
                   setOverallState(PinState.LOW);
                   timer.cancel();
                   timerState = false;
              }
            },50000
        );
    }
    
    public void cancelTimer(){
        timer.cancel();
        timerState = false;
    }
    
    public void monitor() throws InterruptedException {
        if (getState() == PinState.LOW){
            sendNoMotion();
            setOverallState(PinState.LOW);
        }else{
            sendMotion();
            setOverallState(PinState.HIGH);
        }
        
         //create listener
        GpioPinListenerDigital listener = new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                PinState pinState = event.getState();
                PinState mainState = getOverallState();
                if (pinState == PinState.HIGH && mainState == PinState.LOW){
                    setOverallState(PinState.HIGH);
                    sendMotion();
                }else if (pinState == PinState.LOW && mainState  == PinState.HIGH){
                    startTimer();
                   
                }else if (pinState == PinState.HIGH && mainState  == PinState.HIGH){
                    if (getTimerState()){
                        cancelTimer();
                    }
                }
            }
        };
        // register gpio pin listener
        myPin.addListener(listener);
        
        
        // keep program running until user aborts (CTRL-C)
        while(true) {
            Thread.sleep(1000);
        }
    }
    /**
     * returns state of sensor
     */
    public PinState getState(){
        return myPin.getState();
    }
    
    /**
     * returns if sensor is low
     */
    public boolean isLow(){
        return myPin.isLow();
    }
    
    /**
     * returns if sensor is high
     */
    public boolean isHigh(){
        return myPin.isHigh();
    }
    
    /**
     * returns if sensor state is state given
     * @param state Is it this state
     */
    public boolean isState(PinState state){
        return myPin.isState(state);
    }
    
    //These proabaly won't be needed
    
    /**
     * determines if pin state has a debounce interval
     * @param state The pin state to test for debounce delay
     */
    public boolean hasDebounce(PinState state){
        return myPin.hasDebounce(state);
    }
    
     /**
     * returns debounce interval of pin in milliseconds
     * @param state The pin state to find debounce interval
     */
    public int getDebounce(PinState state){
        return myPin.getDebounce(state);
    }
    
    /**
     * sets debounce interval in seconds for all states
     * @param debounce Debounce interval to set
     */

    public void setDebounce(int debounce){
        myPin.setDebounce(debounce);
    }
    
    /**
     * sets debounce interval in seconds for a specific state
     * @param debounce Debounce interval to set
     * @param state State to set debounce interval for
     */
    public void setDebounce(int debounce, PinState... state){
        myPin.setDebounce(debounce, state);
    }
    
    
    //These are for tests 
    
    /**
     * Logs when the sensor changes states
     * @param testTime Seconds tests runs for
     */
    public void logChanges(int testTime) throws InterruptedException {
        int changeCount = 0;
        System.out.println("logChanges started");

        // set shutdown state for this input pin
        myPin.setShutdownOptions(true);

        //create listener
        GpioPinListenerDigital listener = new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }

        };
        // register gpio pin listener
        myPin.addListener(listener);

        System.out.println(" ... complete the GPIO circuit and see the listener feedback here in the console.");

        // keep program running until user aborts (CTRL-C)
        while(changeCount < testTime) {
            Thread.sleep(1000);
            changeCount++;
        }
        
        //remove listener
        myPin.removeListener(listener);
        
        System.out.println("Done log changes");
    }
    
    /**
     * Logs state of Sensor every two seconds
     * @param testTime Seconds tests runs for
     */
    public void logStates(int testTime) throws InterruptedException {
        int stateCount = 0;
        System.out.println("logStates started");

        // set shutdown state for this input pin
        myPin.setShutdownOptions(true);
        
        
         // keep program running until user aborts (CTRL-C)
        while(stateCount < testTime) {
            Thread.sleep(1000);
            System.out.println(" --> PIN STATE " + myPin.getState());
            stateCount++;
        }
        
        System.out.println("Done log states");
    }    
}
