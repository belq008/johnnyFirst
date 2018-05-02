//package com.xinyusoft.xshelllib.adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bokecc.live.util.MyRoundImageview;
//import com.daniulive.smartplayer.VideoModel;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.xinyusoft.xshelllib.R;
//
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * Created by wn on 2016/8/2.
// */
//public class videoListAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener{
//
//    private LayoutInflater minflater;
//    private Context mcontext;
//    private List<VideoModel> mlist;
//    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//    ImageLoader imageLoader = ImageLoader.getInstance();
//    DisplayImageOptions moptions;
//    DisplayImageOptions moptionsbig;
//    private static final int TYPE_ITEM = 0;
//    private static final int TYPE_FOOTER = 1;
//
//
//    public videoListAdapter(Context context,DisplayImageOptions options,DisplayImageOptions moptionsbig,List<VideoModel> list){
//        this.mcontext  = context;
//        this.mlist = list;
//        this.moptions = options;
//        this.moptionsbig = moptionsbig;
//        minflater = LayoutInflater.from(context);
//        imageLoader.init(ImageLoaderConfiguration.createDefault(mcontext));
//    }
//
//    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
//
//    @Override
//    public void onClick(View v) {
//        if (mOnItemClickListener != null) {
//            //注意这里使用getTag方法获取数据
//            mOnItemClickListener.onItemClick(v,(Object)v.getTag());
//        }
//    }
//
//    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//
//
//    //define interface
//    public static interface OnRecyclerViewItemClickListener {
//        void onItemClick(View view , Object data);
//    }
//
//    public void setData(List<VideoModel> list){
//        this.mlist = list;
//    }
//
//
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View view = LayoutInflater.from(mcontext).inflate(R.layout.video_list_item_layout, viewGroup,
//                    false);
//            view.setOnClickListener(this);
//            return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
//        try {
//                VideoModel m = mlist.get(i);
//                myViewHolder.name.setText(m.getAuthorname());
//                myViewHolder.title.setText(m.getRoomtitle());
//                imageLoader.displayImage(m.getHeadurl(), myViewHolder.head, moptions, animateFirstListener);
//                imageLoader.displayImage(m.getImageurl(), myViewHolder.cover, moptionsbig, animateFirstListener);
//                if (m.getTypes().equals("0")) {
//                    myViewHolder.states.setVisibility(View.GONE);
//                } else {
//                    myViewHolder.states.setVisibility(View.VISIBLE);
//                }
//                myViewHolder.itemView.setTag(mlist.get(i));
//        }catch (Exception e){}
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return mlist.size();
//    }
//
//
//
//
//    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//
//        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//        @Override
//        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            if (loadedImage != null) {
//                ImageView imageView = (ImageView) view;
//                // 是否第一次显示
//                boolean firstDisplay = !displayedImages.contains(imageUri);
//                if (firstDisplay) {
//                    // 图片淡入效果
//                    FadeInBitmapDisplayer.animate(imageView, 500);
//                    displayedImages.add(imageUri);
//                }
//            }
//        }
//    }
//}
//
//class MyViewHolder extends RecyclerView.ViewHolder{
//
//    TextView name;
//    TextView title;
//    MyRoundImageview head;
//    ImageView states;
//    ImageView cover;
//    public MyViewHolder(View itemView) {
//        super(itemView);
//
//
//
//        name = (TextView) itemView.findViewById(R.id.author_name);
//        title = (TextView) itemView.findViewById(R.id.video_title);
//        head = (MyRoundImageview) itemView.findViewById(R.id.iv_anchor_head);
//        states = (ImageView) itemView.findViewById(R.id.video_status);
//        cover = (ImageView) itemView.findViewById(R.id.iv_cover);
//
//    }
//
//}
//
//class FootViewHolder extends MyViewHolder{
//
//    public FootViewHolder(View itemView) {
//        super(itemView);
//    }
//}
//
