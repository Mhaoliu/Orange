package com.youku.player.plugin;


public class YoukuPlayerListener {


	// 7个系统播放器必备回调
	public void onVideoSizeChanged(int width, int height) {
	}

	public void onSeekComplete() {
	}

	public void onError( int what, int extra) {
	}

	public void onCompletion() {
	}

	public void onBufferingUpdate( int percent) {
	}

	public void onPrepared() {
	}

	public void onInfo(int what, int extra) {

	}

	/**
	 * 
	 * @param position 当前ms
	 * @param duration 总ms
	 */
	@Deprecated
	public void onCurrentPositionUpdate(int position, int duration) {

	}


	// 视频信息获取的回调

	public void onGetInfoStart() {

	}

	public void onGetInfoFinished() {

	}

	/**
	 * 注意获取信息时的timeout与播放器播放过程中的timeout是不一样的
	 */
	public void onGetInfoTimeout() {

	}

	// TODO 这里是不是有必要把videoUrlInfo的内容简化一下再返回给用户
	public void onGetInfoSuccess() {

	}

	public void onGetInfoFailure(int statusCode, String responseString,
			Throwable throwable) {

	}



	/**
	 * 播放位置变化
	 * 
	 * @param currentPosition
	 *            当前播放位置，毫秒
	 */
	public void onCurrentPositionUpdate(int currentPosition) {
	}

	/**
	 * 硬解出错 TODO 是不是要添加错误码之类的信息？
	 */
	public void onHwDecodeError() {
	}

	/**
	 * 视频缓冲开始
	 */
	public void onStartBuffering() {
	}

	/**
	 * 视频缓冲结束
	 */
	public void onEndBuffering() {
	}

	/**
	 * 网速更新
	 * 
	 * @param count
	 *            单位：kb/s
	 */
	public void onNetworkSpeedChanged(int count) {
	}

	/**
	 * 正片开始播放
	 * 
	 */
	public void onRealVideoStart() {
	}

	/**
	 * 注意播放器播放过程中的timeout与获取信息时的timeout是不一样的
	 */
	public void onTimeOut() {
	}

	/**
	 * 切换清晰度 //TODO 是不是要添加之前和之后的清晰度参数？
	 */
	public void onNotifyChangeVideoQuality() {
	}

	/**
	 * 播放位置变化
	 * 
	 * @param currentIndex
	 *            分片索引
	 * @param ip
	 *            ？
	 */
	public void onVideoIndexUpdate(int currentIndex, int ip) {
	}
}
