package co.sih.presso;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<String> medList;
    private List<String> dosList;
    private List<String> dayList;

    private onItemClickListener mListener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    //getting the context and product list with constructor
    public MedicineAdapter(Context mCtx, List<String> medList, List<String> dosList, List<String> dayList) {
        this.mCtx = mCtx;
        this.medList = medList;
        this.dosList = dosList;
        this.dayList = dayList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_medicine, null);
        return new ProductViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        //medList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(medList.get(position));
        holder.textViewDose.setText(dosList.get(position) + " || " + dayList.get(position));


    }


    @Override
    public int getItemCount() {
        return medList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewDose;
        ImageView imageView;

        public ProductViewHolder(View itemView, final onItemClickListener listener) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDose = itemView.findViewById(R.id.dosageText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}