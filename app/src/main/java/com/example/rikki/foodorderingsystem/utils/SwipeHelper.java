package com.example.rikki.foodorderingsystem.utils;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rikki.foodorderingsystem.CartAdapter;
import com.example.rikki.foodorderingsystem.MainActivity;
import com.example.rikki.foodorderingsystem.model.FoodItem;

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    CartAdapter mCartAdapter;
    OnSwipeDataChange onSwipeDataChange;

    public SwipeHelper(int dragDirs, int swipeDirs, CartAdapter adapter, OnSwipeDataChange onSwipeDataChange) {
        super(dragDirs, swipeDirs);
        mCartAdapter =adapter;
        this.onSwipeDataChange= onSwipeDataChange;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        FoodItem foodItemList = MainActivity.appContext.getCartItems().getCartItemsList().get(viewHolder.getAdapterPosition());
        MainActivity.appContext.getCartItems().getItemQuantity().remove(foodItemList);
        MainActivity.appContext.getCartItems().getCartItemsList().remove(viewHolder.getAdapterPosition());
        mCartAdapter.notifyDataSetChanged();
        onSwipeDataChange.onSwipeCountChange();
    }

    public interface OnSwipeDataChange{
        void onSwipeCountChange();
    }
}
