package com.sena.businessassistantandroid.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sena.businessassistantandroid.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.VH> {

    public interface Callbacks {
        void onEdit(User u);
        void onDelete(User u);
        void onActivate(User u);
    }

    private final List<User> items;
    private final Callbacks callbacks;

    public UsersAdapter(List<User> items, Callbacks callbacks) {
        this.items = items;
        this.callbacks = callbacks;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_row, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        User u = items.get(position);

        // Header
        h.tvHeader.setText(u.name + " - " + u.email);
        h.ivArrow.setImageResource(u.expanded ? R.drawable.ic_expand_less_24 : R.drawable.ic_expand_more_24);

        // Detail
        h.detail.setVisibility(u.expanded ? View.VISIBLE : View.GONE);
        h.tvId.setText(h.itemView.getContext().getString(R.string.user_id_label) + " " + u.id);
        h.tvName.setText(h.itemView.getContext().getString(R.string.user_name_label) + " " + u.name);
        h.tvEmail.setText(h.itemView.getContext().getString(R.string.user_email_label) + " " + u.email);
        h.tvRole.setText(h.itemView.getContext().getString(R.string.user_role_label) + " " + u.role);

        // Toggle
        h.header.setOnClickListener(v -> {
            u.expanded = !u.expanded;
            notifyItemChanged(h.getAdapterPosition());
        });

        // Acciones
        h.btnEdit.setOnClickListener(v -> callbacks.onEdit(u));
        h.btnDelete.setOnClickListener(v -> callbacks.onDelete(u));
        h.btnActivate.setOnClickListener(v -> callbacks.onActivate(u));
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        View header, detail;
        TextView tvHeader, tvId, tvName, tvEmail, tvRole;
        ImageView ivArrow;
        ImageButton btnEdit, btnDelete, btnActivate;

        VH(@NonNull View v) {
            super(v);
            header = v.findViewById(R.id.rowHeader);
            detail = v.findViewById(R.id.rowDetail);
            tvHeader = v.findViewById(R.id.tvHeader);
            ivArrow = v.findViewById(R.id.ivArrow);
            tvId = v.findViewById(R.id.tvId);
            tvName = v.findViewById(R.id.tvName);
            tvEmail = v.findViewById(R.id.tvEmail);
            tvRole = v.findViewById(R.id.tvRole);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnActivate = v.findViewById(R.id.btnView);
        }
    }
}
