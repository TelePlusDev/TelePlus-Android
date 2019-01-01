package net.surina.soundtouch;

@SuppressWarnings({"JniMissingFunction", "unused", "WeakerAccess"})
public class SoundTouch
{
	static
    {
        System.loadLibrary("soundtouch");
    }
	
    private int channels;
    private int samplingRate;
    private int bytesPerSample;
	private float tempo;
	private int pitchSemi;
	private int track;

	public int getChannels()
    {
        return channels;
    }

    public int getSamplingRate()
    {
        return samplingRate;
    }

    public int getBytesPerSample()
    {
        return bytesPerSample;
    }

    public float getTempo()
    {
        return tempo;
    }

    public int getPitchSemi()
    {
        return pitchSemi;
    }



    public SoundTouch(int track, int channels, int samplingRate, int bytesPerSample, float tempo, int pitchSemi)
	{
		this.channels = channels;
		this.samplingRate = samplingRate;
		this.bytesPerSample = bytesPerSample;
		this.tempo = tempo;
		this.pitchSemi = pitchSemi;
		this.track = track;
		setup(track, channels, samplingRate, bytesPerSample, tempo, pitchSemi);
	}
	
    public void putBytes(byte[] input)
    {
    	putBytes(track, input, input.length);
    }
    
    public int getBytes(byte[] output)
    {
    	return getBytes(track, output, output.length);
    }

    public void clearBuffer()
    {
        clearBytes(track);
    }
    
    //call finish after the last bytes have been written
    public void finish()
    {
    	finish(track, Constants.BUFFER_SIZE_PUT);
    }

    private static synchronized native void setup(int track, int channels, int samplingRate, int bytesPerSample, float tempo, int pitchSemi);
    private static synchronized native void putBytes(int track, byte[] input, int length);
    private static synchronized native int getBytes(int track, byte[] output, int toGet);
    private static synchronized native void clearBytes(int track);
    private static synchronized native void finish(int track, int bufSize);
}
