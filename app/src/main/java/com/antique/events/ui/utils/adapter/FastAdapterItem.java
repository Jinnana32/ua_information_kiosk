package com.antique.events.ui.utils.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.items.ModelAbstractItem;

public abstract class FastAdapterItem<M, T extends ModelAbstractItem<?, ?, ?>, VH extends RecyclerView.ViewHolder> extends ModelAbstractItem<M, T, VH> {

    private long mHeaderId;
    private String mHeader;

    public FastAdapterItem(M m) {
        super(m);
    }

    public T withHeader(String header) {
        mHeader = header;
        return (T) this;
    }

    public T withHeaderId(long headerId) {
        mHeaderId = headerId;
        return (T) this;
    }

    public long getHeaderId() {
        return mHeaderId;
    }

    public String getHeader() {
        return mHeader;
    }
}
