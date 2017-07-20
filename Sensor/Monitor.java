
/**
 * Monitors activity around a Raspberry Pie using an infared sensor and bluetooth detection
 * 
 * @author Jake Gluck
 * @version 0.0
 */

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
public class Monitor implements MotionListener, BlueListener {
    Sensor sensor;
    BlueScan scanner;

    /**
     * Constructor for objects of class Monitor
     */
    public Monitor() {
        sensor = new Sensor(RaspiPin.GPIO_07);
        scanner = new BlueScan("E0:99:71:E:4B:2B");
    }

    public void startMonitoring() throws InterruptedException {
        System.out.println("in monitor");
        sensor.addListener(this);
        scanner.addListener(this);
        sensor.monitor();
        PinState motion = sensor.getState();
        scanner.monitor();
        while(true) {
           Thread.sleep(1000);
        }
    }
    
    @Override
    public void motionPresent(){
        System.out.println("motion present");
    }
    
     @Override
    public void motionAbsent(){
        System.out.println("motion absent");
    }

	@Override
	public void bluetoothPresent() {
		System.out.println("Device Present");		
	}

	@Override
	public void bluetoothAbsent() {
		System.out.println("Device Absent");		
	}    
}
