package com.debla.hashpet.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.ProductSellDetail;
import com.debla.hashpet.Model.SellerInfo;
import com.debla.hashpet.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class IndexListView extends ListView {
	private IndexAdapter indexAdapter;// 关联数据源的Adapter
	// 保存已加载的新闻数据的集合
	private Gson gson;
	//获取的整个数据集
	private List<ProductSellDetail> total_list;
	private List<ProductSellDetail> list = new ArrayList<>();
	private LinearLayout mHeaderView = null;//顶部的布局
	private TextView headerInfo;//顶部显示文本信息的控件,显示下拉刷新、松手刷新、正在加载等信息以及刷新时间
	private ImageView imageInfo;//顶部显示的图片信息的控件，下拉时显示向下的箭头，释放时显示向上的箭头
	private ProgressBar mProgressBar;//顶部进度条
	private int mHeaderHeight;//下拉列表头部View的高度
	private Boolean threadOK = false;
	// private boolean hasNew;//是否包含新记录的标志
	private int mCurrentScrollState;//当前的滚动条状态
	// private MyOpenHelper mHelper;//数据库辅助类
	// private SQLiteDatabase mDB;//数据库封装类
	//下拉刷新过程中，自定义的几种状态
	private final static int NONE_PULL_REFRESH = 0;//正常状态
	private final static int ENTER_PULL_REFRESH = 1;//进入下拉刷新状态
	private final static int OVER_PULL_REFRESH = 2;// 进入松手刷新状态
	private final static int EXIT_PULL_REFRESH = 3;//松手,反弹后加载状态
	private int mPullRefreshState = 0; //记录刷新状态，默认为正常状态
	private float mDownY;//按下时的坐标
	private float mMoveY;//移动后的坐标
	private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");//时间的显示格式
	// Handler处理消息时，所包含的一些消息标志
	private final static int REFRESH_BACKING = 0;//反弹中
	private final static int REFRESH_BACKED = 1; //反弹结束后，需要刷新
	private final static int REFRESH_RETURN = 2;//反弹结束后，不需要刷新
	private final static int REFRESH_DONE = 3;//数据加载结束
	//下拉刷新次数
    private static int REFRESH_COUNT = 0;

	public IndexListView(Context context) {
		this(context, null);
	}

	public IndexListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);//进行初始化操作
		setOnScrollListener(new MyScrollListener());//添加滚动事件监听器
		setSelection(1);//设置选中项，默认为0，头部View也算一项，设置为1表示不显示头部View
	}

	public void init(final Context context) {
		/*
		 * mHelper = new MyOpenHelper(context, "news.db", null, 1);
		 * mDB = mHelper.getReadableDatabase();//
		 */

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//获取布局填充器
		mHeaderView = (LinearLayout) layoutInflater.inflate(
				R.layout.refresh_header, null);// 将布局文件转换成View对象
		addHeaderView(mHeaderView);//将头部View添加到下拉列表中去
		// orderList =
		// getData("select * from news_tb order by _id desc limit 0,6",
		// null);//获取初始信息
		total_list = getDataFromService();
		indexAdapter = new IndexAdapter(context);
		this.setAdapter(indexAdapter);
		//根据Id找到布局中相应的控件
		headerInfo = (TextView) mHeaderView.findViewById(R.id.headerInfo);
		imageInfo = (ImageView) mHeaderView.findViewById(R.id.imageInfo);
		mProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.progressbar);//进度条
		reset();//初始状态
		measureView(mHeaderView);//指定头部控件的大小
		mHeaderHeight = mHeaderView.getMeasuredHeight();//获取头部View的高度
	}

	/**
	 * 返回数据集
	 * @return
	 */
	public List<ProductSellDetail> getDataList(){
		return this.list;
	}

/*	class GetDataTask extends FutureTask<Boolean> {
		public GetDataTask() {
			super(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					return null;
				}

			});
		}
	}*/

	private List<ProductSellDetail> getDataFromService() {
		// TODO Auto-generated method stub
		gson = new Gson();
		HttpUtil httpUtil = new HttpUtil();
		String url = HttpUtil.getUrl(getContext())+"/getIndexProduct";
		Map<String,String> params = new HashMap<>();
		threadOK = false;
		httpUtil.postRequest(url, params, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.e("debug","从服务器读取首页信息失败");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {

				String JSON = response.body().string();
				Log.e("debug",JSON);
				Type jsonType = new TypeToken<BaseJsonObject<List<ProductSellDetail>>>(){}.getType();
				BaseJsonObject<SellerInfo> jsonObject = null;
				jsonObject = (BaseJsonObject) gson.fromJson(JSON, jsonType);
				list = (List<ProductSellDetail>) jsonObject.getResult();
				threadOK =true;
			}
		});
		return list;
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();//获取控件的布局参数
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);//默认宽度填充父容器，高度为内容包裹
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);//获取宽度要求
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);//子控件的高度由父容器指定,是一个精确值，而不管子控件内容的多少
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);//子控件的高度不受限制，可以是任意值
		}
		child.measure(childWidthSpec, childHeightSpec);//子控件的宽度和高度
	}


	//根据查询语句获取数据
	/*
	 * public LinkedList<Map<String, Object>> getData(String sql, String[] args)
	 * { LinkedList<Map<String, Object>> list = new LinkedList<Map<String,
	 * Object>>(); Cursor cursor = mDB.rawQuery(sql, args);//查询符合条件的记录while
	 * (cursor.moveToNext()) {//循环遍历每条记录，获取相应数据，并将数据保存到集合中Map<String, Object>
	 * item = new HashMap<String, Object>(); item.put("image",
	 * cursor.getString(cursor.getColumnIndex("image"))); item.put("title",
	 * cursor.getString(cursor.getColumnIndex("title"))); item.put("info",
	 * cursor.getString(cursor.getColumnIndex("info"))); list.add(item); }
	 * return list; }
	 */
	public class MyScrollListener implements OnScrollListener {//滚动事件监听器
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			mCurrentScrollState = scrollState;//记录当前的滚动条的状态
		}

		public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {//滚动时调用
			if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
					&& firstVisibleItem == 0
					&& (mHeaderView.getBottom() >= 0 && mHeaderView.getBottom() < mHeaderHeight)) {
				//手指在拉动滚动条、头部View还没有完全显示出来时，进入且仅进入下拉刷新状态
				if (mPullRefreshState == NONE_PULL_REFRESH) {
					mPullRefreshState = ENTER_PULL_REFRESH;
				}
			} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
					&& firstVisibleItem == 0
					&& (mHeaderView.getBottom() >= mHeaderHeight)) {
				//滚动达到或超过头部View的高度，进入松手刷新状态
				if (mPullRefreshState == ENTER_PULL_REFRESH
						|| mPullRefreshState == NONE_PULL_REFRESH) {
					mPullRefreshState = OVER_PULL_REFRESH;
					//下面是进入松手刷新状态需要做的一个显示改变
					mDownY = mMoveY;//记录进入松手状态时的坐标
					headerInfo.setText("松手即可刷新\n上次刷新时间："
							+ mSimpleDateFormat.format(new Date()));
					imageInfo.setImageResource(R.drawable.up_arrow);
				}
			} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
					&& firstVisibleItem != 0) {
				//当可见项不是第一项时，即还没有拖动到顶部
				if (mPullRefreshState == ENTER_PULL_REFRESH) {
					mPullRefreshState = NONE_PULL_REFRESH;
				}
			} else if (mCurrentScrollState == SCROLL_STATE_FLING
					&& firstVisibleItem == 0) {
				//飞滑状态，不能显示出header，也不能影响正常的飞滑
				if (mPullRefreshState == NONE_PULL_REFRESH) {
					setSelection(1);
				}
			}
		}
	}

	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN://记下按下位置，改变
			mDownY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE://移动时手指的位置
			mMoveY = ev.getY();
			if (mPullRefreshState == OVER_PULL_REFRESH) {
				mHeaderView.setPadding(0, (int) ((mMoveY - mDownY) / 2), 0,
						mHeaderView.getPaddingBottom());
			}
			break;
		case MotionEvent.ACTION_UP://松手时，将会回弹，并且隐藏头部View
			if (mPullRefreshState == OVER_PULL_REFRESH
					|| mPullRefreshState == ENTER_PULL_REFRESH) {
				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						while (mHeaderView.getPaddingTop() > 1) {
							Message msg = mHandler.obtainMessage();
							msg.what = REFRESH_BACKING;//发送处理反弹的消息
							mHandler.sendMessage(msg);
						}
						Message message = mHandler.obtainMessage();
						if (mPullRefreshState == OVER_PULL_REFRESH) {//如果原来是松开
							message.what = REFRESH_BACKED;//反弹完成，加载数据
						} else {//如果原来只是下拉
							message.what = REFRESH_RETURN;//反弹完成，不需要加载数据
						}
						mHandler.sendMessage(message);
						timer.cancel();
					}
				}, 0, 100);
			}
			;
			break;
		}
		return super.onTouchEvent(ev);
	}

	private Handler mHandler = new Handler() {//创建Handler对象，发送、接收和处理消息
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_BACKING://处理反弹，每次让上边距为前一次的0.75
				mHeaderView.setPadding(0,
						(int) (mHeaderView.getPaddingTop() * 0.75f), 0,
						mHeaderView.getPaddingBottom());
				break;
			case REFRESH_BACKED://加载数据
				imageInfo.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.VISIBLE);
				headerInfo.setText("正在刷新...");
				mHandler.postDelayed(new Runnable() {
					public void run() {
						refreshing();
					}
				}, 500);
				break;
			case REFRESH_RETURN://反弹结束，不需要加载数据，恢复原状
			case REFRESH_DONE://刷新结束后，恢复原始默认状态
				reset();
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Message message = mHandler.obtainMessage();
						message.what = REFRESH_DONE;
						mHandler.sendMessage(message);
					}
				},1000);
				break;
			default:
				break;
			}
		}
	};

	public void reset() {//恢复原来的状态
		headerInfo.setText("下拉刷新");
		mProgressBar.setVisibility(View.GONE);
		imageInfo.setVisibility(View.VISIBLE);
		imageInfo.setImageResource(R.drawable.down_arrow);
		mHeaderView.setPadding(0, 0, 0, mHeaderView.getPaddingBottom());
		mPullRefreshState = NONE_PULL_REFRESH;
		setSelection(1);
	}

	public void refreshing() {
		List<ProductSellDetail> total_list = getDataFromService();
		if (total_list != null && total_list.size() != 0) {
			list.clear();
            for(int i=0;i<total_list.size();i++){
                list.add(total_list.get(i));
            }
            REFRESH_COUNT++;
			indexAdapter.notifyDataSetChanged();//更新列表显示
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mPullRefreshState = EXIT_PULL_REFRESH;//改变状态，发送消息
					Message message = mHandler.obtainMessage();
					message.what = REFRESH_DONE;
					mHandler.sendMessage(message);
				}
			},3000);
		}
	}

	//首页ListView的Adapter配置
	class IndexAdapter extends BaseAdapter {
        public ImageLoader imageLoader; //用来下载图片的类
        private String url;

        public IndexAdapter(Context context){
            imageLoader = new ImageLoader(context);
            url = HttpUtil.getUrl(context)+"/getImage";
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v;
			if (convertView == null) {
				v = View.inflate(getContext(), R.layout.lv_product, null);
			} else {
				v = convertView;
			}
			TextView tvPrdName = (TextView) v.findViewById(R.id.lv_prd_title);
            tvPrdName.setText(list.get(position).getProName());
            TextView tvPrice = (TextView) v.findViewById(R.id.lv_prd_price);
            tvPrice.setText("￥"+list.get(position).getProPrice());
            TextView tvBrief = (TextView) v.findViewById(R.id.lv_prd_brief);
            tvBrief.setText(list.get(position).getProBrief());
            ImageView prodImage = (ImageView) v.findViewById(R.id.lv_prd_img);
            imageLoader.DisplayImage(url+"?urlId="+list.get(position).getProSrc(),prodImage);
			return v;
		}

	}
}
