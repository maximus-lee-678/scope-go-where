package ict2105.team02.application.ui.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ict2105.team02.application.R
import ict2105.team02.application.ui.wash.WashActivity
import ict2105.team02.application.databinding.FragmentScopeDetailBinding
import ict2105.team02.application.ui.sample.SampleActivity
import ict2105.team02.application.ui.equipment.EquipLogFragment
import ict2105.team02.application.ui.main.MainActivity
import ict2105.team02.application.ui.sample.ScanDialogFragment
import ict2105.team02.application.viewmodel.ScopeDetailViewModel
import java.text.SimpleDateFormat

const val KEY_ENDOSCOPE_SERIAL = "SN"
const val KEY_ENDOSCOPE_MODEL = "MODEL"
const val KEY_ENDOSCOPE_STATUS = "STATUS"

class ScopeDetailFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentScopeDetailBinding

    private val viewModel by viewModels<ScopeDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScopeDetailBinding.inflate(inflater)

        binding.equipmentBannerLayout.visibility = View.GONE
        binding.scopeDetailLayout.visibility = View.INVISIBLE

        viewModel.scopeDetail.observe(this) {
            binding.equipmentNameTextView.text = it.scopeModel + it.scopeSerial
            binding.modelTextView.text = it.scopeModel
            binding.typeTextView.text = it.scopeType
            binding.serialTextView.text = it.scopeSerial.toString()
            binding.statusTextView.text = it.scopeStatus
            binding.nextSampleTextView.text = it.nextSampleDate.toString()
            binding.nextSampleTextView.text = SimpleDateFormat("dd/MM/yyyy").format(it.nextSampleDate)

            when(it.scopeStatus) {
                "Circulation" -> {
                    binding.statusIconImageView.setImageResource(R.drawable.outline_inventory_2_24)
                }
                "Sampling" -> {
                    binding.statusIconImageView.setImageResource(R.drawable.outline_access_time_24)
                }
                "Washing" -> {
                }
            }
        }

        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val serial = arguments?.getInt(KEY_ENDOSCOPE_SERIAL)
        if (serial != null) {
            viewModel.fetchScopeDetail(serial) {
                requireActivity().runOnUiThread {
                    binding.loadScopeProgressIndicator.visibility = View.GONE
                    binding.equipmentBannerLayout.visibility = View.VISIBLE
                    binding.scopeDetailLayout.visibility = View.VISIBLE
                }
            }
        }

        viewModel.scopeDetail.observe(this) {
            when(it.scopeStatus) {
                "Circulation" -> {
                }
                "Sampling" -> {
                    binding.washButton.visibility = View.GONE
                }
                "Washing" -> {
                    binding.sampleButton.visibility = View.GONE
                }
            }
        }

        binding.washButton.setOnClickListener {
            val intent = Intent(activity, WashActivity::class.java)
            if(serial != null){
                val scopeHashMap = HashMap<String, Any>()
                scopeHashMap["scopeSerial"] = viewModel.scopeDetail.value!!.scopeSerial
                scopeHashMap["scopeModel"] = viewModel.scopeDetail.value!!.scopeModel
                scopeHashMap["scopeBrand"] = viewModel.scopeDetail.value!!.scopeBrand
                intent.putExtra("scopeDetails", scopeHashMap)
            }
            activity?.startActivity(intent)
            activity?.finish()
        }
        binding.sampleButton.setOnClickListener {
//            val intent = Intent(getActivity(), SampleActivity::class.java)
//            getActivity()?.startActivity(intent)
            ScanDialogFragment().show(childFragmentManager,"")
        }

        binding.viewLogsButton.setOnClickListener{
            // replace with last fragment
            var serial = viewModel.scopeDetail.value!!.scopeSerial
            var model = viewModel.scopeDetail.value!!.scopeModel
            var status = viewModel.scopeDetail.value!!.scopeStatus

            val fragment = EquipLogFragment.newInstance(serial,model,status)
            (activity as MainActivity).navbarNavigate(fragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(serialNo: Int) = ScopeDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_ENDOSCOPE_SERIAL, serialNo)
            }
        }
    }

    }