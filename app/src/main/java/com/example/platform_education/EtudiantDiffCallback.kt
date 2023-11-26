// EtudiantDiffCallback.kt
import androidx.recyclerview.widget.DiffUtil
import com.example.platform_education.Etudiant

class EtudiantDiffCallback(
    private val oldList: List<Etudiant>,
    private val newList: List<Etudiant>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].NumInscrit == newList[newItemPosition].NumInscrit
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
