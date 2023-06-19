import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.AddIzinInstruktur
import com.example.gofit.IzinInstrukturActivity
import com.example.gofit.R
import com.example.gofit.models.Izin
import com.shashank.sony.fancytoastlib.FancyToast
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class IzinAdapter(private var izinInstrukturList: List<Izin>, context: Context) :
    RecyclerView.Adapter<IzinAdapter.ViewHolder>() {

    private var filteredizinInstrukturList: MutableList<Izin>
    private val context: Context

    init{
        filteredizinInstrukturList = ArrayList(izinInstrukturList)
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_izin_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredizinInstrukturList.size
    }

    fun setIzinInstrukturList(izinInstrukturList: Array<Izin>){
        this.izinInstrukturList = izinInstrukturList.toList()
        filteredizinInstrukturList = izinInstrukturList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){

        val data = izinInstrukturList[position]
        val izinInstruktur = filteredizinInstrukturList[position]
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        holder.tvTanggalIzinInstruktur.text = "Tanggal Izin : ${data.TANGGAL_IZIN}"
        holder.tvTanggalPengajuan.text = "Tanggal Pengajuan : ${data.TANGGAL_PENGAJUAN}"
        holder.tvKeteranganIzin.text = "Keterangan Izin: ${data.KETERANGAN_IZIN}"
        holder.tvStatusIzin.text = "Status Konfirmasi : ${data.TANGGAL_KONFIRMASI} - ${data.STATUS_KONFIRMASI}"
        if(holder.tvStatusIzin.text == "Status Konfirmasi : null - null"){
            holder.tvStatusIzin.text = "Belum Dikonfirmasi"
        }

//        holder.tvIdIzinInstruktur.text = "ID IZIN : " + izinInstruktur.ID_IZIN.toString()
//        holder.tvIdInstruktur.text = "ID Instruktur : " + izinInstruktur.ID_INSTRUKTUR.toString()
//        holder.tvNamaInstruktur.text = "Nama Instruktur : " + izinInstruktur.NAMA_INSTRUKTUR
//        holder.tvTanggalIzinInstruktur.text = "Tgl Izin : " + izinInstruktur.TANGGAL_IZIN.format(formatter)
//        holder.tvKeteranganIzin.text = "Keterangan : " + izinInstruktur.KETERANGAN_IZIN
//        holder.tvTanggalPengajuan.text = "Tgl Pengajuan : " + izinInstruktur.TANGGAL_PENGAJUAN.format(formatter)
//        holder.tvTanggalKonfirmasiIzin.text ="Tgl Konfirmasi : "  + izinInstruktur.TANGGAL_KONFIRMASI.format(formatter)
//        holder.tvStatusIzin.text = "Status Konfirmasi : " + izinInstruktur.STATUS_KONFIRMASI



        holder.cvIzinInstruktur.setOnClickListener{
//            val i = Intent(context, AddIzinInstruktur::class.java)
//            i.putExtra("id",izinInstruktur.ID_IZIN)
//            if(context is IzinInstrukturActivity)
//                context.startActivityForResult(i, IzinInstrukturActivity.LAUNCH_ADD_ACTIVITY)
            FancyToast.makeText(context,data.STATUS_KONFIRMASI, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
         }
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvIdIzinInstruktur: TextView
        var tvIdInstruktur: TextView
        var tvNamaInstruktur: TextView
        var tvTanggalIzinInstruktur: TextView
        var tvTanggalPengajuan: TextView
        var tvStatusIzin: TextView
        var tvKeteranganIzin: TextView
        var tvTanggalKonfirmasiIzin: TextView
        var cvIzinInstruktur: CardView

        init{

            tvIdIzinInstruktur = itemView.findViewById(R.id.tvIdIzinInstruktur)
            tvIdInstruktur = itemView.findViewById(R.id.tvIdInstruktur)
            tvNamaInstruktur = itemView.findViewById(R.id.tvNamaInstruktur)
            tvTanggalIzinInstruktur = itemView.findViewById(R.id.tvTanggalIzinInstruktur)
            tvKeteranganIzin = itemView.findViewById(R.id.tvKeteranganIzin)
            tvTanggalPengajuan = itemView.findViewById(R.id.tvTanggalMelakukanIzin)
            tvStatusIzin = itemView.findViewById(R.id.tvStatusIzin)
            tvTanggalKonfirmasiIzin = itemView.findViewById(R.id.tvTanggalKonfirmasiIzin)
            cvIzinInstruktur = itemView.findViewById(R.id.cv_izin)
        }
    }
}