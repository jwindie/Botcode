package playground;

public final class Time {

  private static int frameCount;
  private static float frameRate;
	private static float time = 0;
	private static float deltaTime = 0;

  public static void update  (int frame, float frameRate) {
    frameCount ++;
    time += deltaTime = (1f/ (Time.frameRate = frameRate));
  }

  public static int getFrameCount() {
    return frameCount;
  }

  public static float getDeltaTime() {
    return deltaTime;
  }

  public static float getFrameRate() {
    return frameRate;
  }

  public static float getTime() {
    return time;
  }
}