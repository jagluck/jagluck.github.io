import tinyb.*;
import java.util.*;
import java.time.*;

public class BlueScan {
	BluetoothManager manager;
	String target_addr;
	private List<BlueListener> listeners;
	//Timer timer;
	boolean deviceState; //, deviceState;
	
	
	
	public BlueScan(String addr) {
		manager.getBluetoothManager();
		target_addr = addr;
		listeners = new ArrayList<BlueListener>();
		/*timer = new Timer();
		timerState = false;*/
		deviceState = false; 
	}
	
	public void addListener(BlueListener toAdd) {
		listeners.add(toAdd);	
	}
	
	public void sendPresent() {
		for (BlueListener b : listeners) 
			b.bluetoothPresent();
	}
	
	public void sendAbsent() {
		for (BlueListener b : listeners)
			b.bluetoothAbsent();
	}
	
	public boolean getDeviceState() {
		return deviceState;
	}
	
	public void setDeviceState(boolean curr_state) {
		deviceState = curr_state;
	}
	
	/*public boolean getTimerState() {
		return timerState;
	}
	
	public void startTimer() {
		timerState = true;
		timer.schedule( new TimerTask() {
			public void run() {
				sendAbsent();
				setDeviceState(false);
				timer.cancel();
				timerState = false;
			}
		}, 5000);
	}
	
	public void cancelTimer() {
		timer.cancel();
		timerState = false;
	}*/
	
	public void monitor() throws InterruptedException {
		boolean discoveryStarted = manager.startDiscovery();	
		boolean found = false;	
		List<BluetoothDevice> devices;
		
		if (getDeviceState) {
			sendPresent();
			setDeviceState(true);
		
		} else {
			sendAbsent();
			setDeviceState(false);
			
		}
		
		while (true) {
			devices = manager.getDevices();
			
			for (BluetoothDevice target : devices) {
				if (target.getAddress().equals(args[0])) { 
					found = true;
					break;
				}
			}

			if (found) {
				found = false;
				sendPresent();
				setDeviceState(true);

			}  else {
				sendAbsent();
				setDeviceState(false);

			}

			Thread.sleep(5000);
		}
	}
			

/*	public static void main(String[] args) throws InterruptedException{
		BluetoothManager manager = BluetoothManager.getBluetoothManager();
		List<BluetoothDevice> devices;
		boolean found = false;
		boolean discoveryStarted = manager.startDiscovery();

		while (true) {
			devices = manager.getDevices();

			for (BluetoothDevice device : devices) {
				if (device.getAddress().equals(args[0])) {
					found = true;
					break;
				}
			}

			if (found) {
				found = false;
				System.out.println("Device present");

			}  else {
				System.out.println("Device absent");

			}

			Thread.sleep(5000);
		}
	}*/
}
