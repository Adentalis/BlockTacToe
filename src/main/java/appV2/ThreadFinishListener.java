package appV2;

public interface ThreadFinishListener extends Runnable{
	
	@Override
	default public void run() {
		onSuccess();
	}
	
	public void onSuccess();
}
