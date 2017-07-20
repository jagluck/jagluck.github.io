
/**
 * Tests for Sensor
 * 
 * @author Jake Gluck
 * @version 0.0
 */

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class ir
{
    
    public static void main(String args[]) throws InterruptedException {
       Monitor monitor = new Monitor();
       monitor.startMonitoring();
    }
    
    public static void test() throws InterruptedException{
        Sensor sensor = new Sensor(RaspiPin.GPIO_07);
        sensor.logStates(10);
        System.out.println("get current state " + sensor.getState());
        System.out.println("is low? " + sensor.isLow());
        System.out.println("is high? " + sensor.isHigh());
        System.out.println("is state low? " + sensor.isState(PinState.LOW));
        System.out.println("is state high? " + sensor.isState(PinState.HIGH));
        System.out.println("does low have debounce? " + sensor.hasDebounce(PinState.LOW));
        System.out.println("does high have debounce? " + sensor.hasDebounce(PinState.HIGH));
        System.out.println("what is lows debounce? " + sensor.getDebounce(PinState.LOW));
        System.out.println("what is highs debounce? " + sensor.getDebounce(PinState.HIGH));
        sensor.setDebounce(2);
        System.out.println("set all debounces to 2 seconds");
        System.out.println("does low have debounce? " + sensor.hasDebounce(PinState.LOW));
        System.out.println("does high have debounce? " + sensor.hasDebounce(PinState.HIGH));
        System.out.println("what is lows debounce? " + sensor.getDebounce(PinState.LOW));
        System.out.println("what is highs debounce? " + sensor.getDebounce(PinState.HIGH));
        sensor.setDebounce(1, PinState.HIGH);
        System.out.println("set highs debounces to 1 seconds");
        System.out.println("does low have debounce? " + sensor.hasDebounce(PinState.LOW));
        System.out.println("does high have debounce? " + sensor.hasDebounce(PinState.HIGH));
        System.out.println("what is lows debounce? " + sensor.getDebounce(PinState.LOW));
        System.out.println("what is highs debounce? " + sensor.getDebounce(PinState.HIGH));
    }
}