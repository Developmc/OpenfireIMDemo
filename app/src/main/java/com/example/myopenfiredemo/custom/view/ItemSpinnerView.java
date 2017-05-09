package com.example.myopenfiredemo.custom.view;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.custom.lisenter.OnItemClickListener;
import com.example.myopenfiredemo.custom.lisenter.OnSpinnerUpdateListener;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**抽取itemView
 * Created by clement on 16/12/15.
 */

public class ItemSpinnerView extends LinearLayout{
    @BindView(R.id.tv_label)
    AppCompatTextView tv_label;
    @BindView(R.id.spinner)
    AppCompatSpinner spinner;
    @BindView(R.id.layout)
    RelativeLayout layout;
    private OnItemClickListener onItemClickListener;
    private OnSpinnerUpdateListener onSpinnerUpdateListener;
    private Context mContext;
    private ArrayAdapter<String> typeAdapter;
    private List<String> typeItems;
    private boolean hasTouch = false ;

    public ItemSpinnerView(Context context) {
        this(context,null);
    }

    public ItemSpinnerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ItemSpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(context);
    }
    private void initView(Context context){
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_item_spinner,this,true);
        ButterKnife.bind(this,rootView);
        //设置点击事件
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(view);
                }
            }
        });
    }

    public AppCompatTextView getTvLabel() {
        return tv_label;
    }

    public AppCompatSpinner getSpinner() {
        return spinner;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnSpinnerUpdateListener(OnSpinnerUpdateListener onSpinnerUpdateListener) {
        this.onSpinnerUpdateListener = onSpinnerUpdateListener;
    }

    /**现在绑定数据时，不执行回调
     * @param typeItems
     */
    public void setDatas(final List<String> typeItems){
        this.hasTouch = false;
        this.typeItems = typeItems ;
        //如果数据源为空，则spinner不可点击
        if(typeItems==null || typeItems.isEmpty()){
            spinner.setClickable(false);
            return;
        }
        else{
            spinner.setClickable(true);
        }
        typeAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, typeItems);
        typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(typeAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //如果没有回调onTouch事件,不处理Item选择事件
                if(!hasTouch){
                    return;
                }
                try{
                    Field field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
                    field.setAccessible(true);   //设置可访问
                    field.setInt(spinner,AdapterView.INVALID_POSITION);  //恢复默认值
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(onSpinnerUpdateListener!=null){
                    onSpinnerUpdateListener.onUpdate(i);
                }
                //重置值
                hasTouch = false;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hasTouch = true;
                return false;
            }
        });
    }

    /**更新位置，会执行回调
     * @param position
     * @param animate
     */
    public void setSelection(int position,boolean animate){
        if(typeItems==null || typeItems.isEmpty()){
            return;
        }
        spinner.setSelection(position, animate);
        if(onSpinnerUpdateListener!=null){
            onSpinnerUpdateListener.onUpdate(position);
        }
    }
}
