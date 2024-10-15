import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nuka2024_try.R
import com.example.nuka2024_try.ui.stores.Store

class StoresAdapter(private val stores: List<Store>) : RecyclerView.Adapter<StoresAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = stores[position]
        holder.bind(store)
    }

    override fun getItemCount(): Int = stores.size

    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.store_image)

        fun bind(store: Store) {
            Glide.with(itemView.context)
                .load(store.imageUrl)  // ここで Store オブジェクトの imageUrl を使用
                .into(imageView)       // imageView に画像を表示
        }

    }
}
