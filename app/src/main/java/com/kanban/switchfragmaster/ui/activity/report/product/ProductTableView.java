package com.kanban.switchfragmaster.ui.activity.report.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.ProductTableInfoAdapter;
import com.kanban.switchfragmaster.data.ProductTableInfo;

import java.util.ArrayList;

/**
 * Created by LongQ on 2017/12/12.
 */

public class ProductTableView extends LinearLayout{

    private Context context;
    private ListView mListView;
    private ProductTableInfoAdapter adapter;
    private ArrayList<ProductTableInfo> productList;
    public ProductTableView(Context context, ArrayList<ProductTableInfo> productList) {
        super(context);
        this.context = context;
        this.productList = productList;
        initView();
        initData();
    }
    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.fragment_product_table_info,this);
        mListView = findViewById(R.id.FRAGMENT_PRODUCT_TABLE_INFO_LISTVIEW);
    }

    protected void initData() {
        if(productList!=null){
            adapter = new ProductTableInfoAdapter(context, productList);
            mListView.setAdapter(adapter);
        }
    }
}
