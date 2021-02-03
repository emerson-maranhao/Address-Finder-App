package br.com.emdev.addressfinder.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.emdev.addressfinder.data.response.AddressResponse
import br.com.emdev.addressfinder.databinding.ItemListBinding

private var _binding: ItemListBinding? = null
private val binding
    get() = _binding!!

class AddressAdapter(private val list: List<AddressResponse>) :
    RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        _binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    inner class MyViewHolder(itemView: ItemListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private var cep = itemView.tvCepResult
        private var rua = itemView.tvRuaResult
        private var bairro = itemView.tvBairroResult
        private var complemento = itemView.tvComplementoResult
        fun bindView(address: AddressResponse) {
            with(itemView) {
                cep.text = address.cep
                rua.text = address.logradouro
                bairro.text = if (address.bairro.isEmpty()) "Não foi informado" else address.bairro
                complemento.text =if (address.complemento.isEmpty()) "Não foi informado" else address.complemento

            }

        }
    }
}

