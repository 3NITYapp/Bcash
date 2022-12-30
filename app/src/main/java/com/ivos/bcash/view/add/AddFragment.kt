package com.ivos.bcash.view.add

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ivos.bcash.R
import com.ivos.bcash.databinding.FragmentAddBinding
import com.ivos.bcash.data.model.Expense
import com.ivos.bcash.util.modifyIntoDatePicker
import com.ivos.bcash.util.parseDouble
import com.ivos.bcash.view.base.BaseFragment
import com.ivos.bcash.view.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddFragment : BaseFragment<FragmentAddBinding, BaseViewModel>() {

    private val args: AddFragmentArgs by navArgs()
    override val viewModel: BaseViewModel by activityViewModels()

    private var expFromArgs: Expense? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()

        if (args.expense != null) {
            expFromArgs = args.expense
            onEditSaved(expFromArgs!!)
        }
    }

    private fun initializeView() {

        val expenseTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_autocomplete_layout,
//            Constants.expenseType
            listOf(
                requireContext().getString(R.string.income),
                requireContext().getString(R.string.expense)
            )
        )

        val expenseTagAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_autocomplete_layout,
//            Constants.expenseTags
            listOf(
                requireContext().getString(R.string.housing),
                requireContext().getString(R.string.transportation),
                requireContext().getString(R.string.food),
                requireContext().getString(R.string.utilities),
                requireContext().getString(R.string.insurance),
                requireContext().getString(R.string.healthcare),
                requireContext().getString(R.string.saving_and_debts),
                requireContext().getString(R.string.personal_spending),
                requireContext().getString(R.string.entertainment),
                requireContext().getString(R.string.miscellaneous)
            )
        )

        with(binding) {

            addLayout.addType.setAdapter(expenseTypeAdapter)
            addLayout.addTag.setAdapter(expenseTagAdapter)

            addLayout.addDate.modifyIntoDatePicker(
                requireContext(),
                "yyyy-MM-dd",
                Date()
            )
            buttonSave.setOnClickListener {
                binding.addLayout.apply {
                    val (title, amount, type, tag, date, desc) = getExpenseBinding()
                    when {
                        title.isEmpty() -> {
                            this.addTitle.error = requireContext().getString(R.string.error_title)
                        }
                        amount.isNaN() -> {
                            this.addAmount.error = requireContext().getString(R.string.error_amount)
                        }
                        type.isEmpty() -> {
                            this.addType.error = requireContext().getString(R.string.error_type)
                        }
                        tag.isEmpty() -> {
                            this.addTag.error = requireContext().getString(R.string.error_tag)
                        }
                        date.isEmpty() -> {
                            this.addDate.error = requireContext().getString(R.string.error_date)
                        }
                        desc.isEmpty() -> {
                            this.addDesc.error =
                                requireContext().getString(R.string.error_description)
                        }
                        else -> {
                            if (binding.addLayout.tvAddHeader.text == requireContext().getString(R.string.edit)) {
                                expFromArgs = getExpenseBinding()
                                expFromArgs!!.id = args.expense!!.id
                                viewModel.updateExpense(expFromArgs!!).run {
                                    toast(getString(R.string.successfully_update))
                                    findNavController().navigate(R.id.action_addFragment_to_mainFragment)
                                }

                            } else {
                                viewModel.insertExpense(getExpenseBinding()).run {
                                    toast(getString(R.string.successfully_save))
                                    findNavController().navigate(R.id.action_addFragment_to_mainFragment)
                                }
                            }

                        }
                    }
                }
            }


        }
    }

    private fun onEditSaved(expense: Expense) = with(binding.addLayout) {
        tvAddHeader.text = requireContext().getString(R.string.edit)
        addTitle.text = SpannableStringBuilder(expense.title)
        addAmount.text = SpannableStringBuilder(expense.amount.toInt().toString())
        addType.text = SpannableStringBuilder(expense.type)
        addTag.text = SpannableStringBuilder(expense.tag)
        addDate.text = SpannableStringBuilder(expense.date)
        addDesc.text = SpannableStringBuilder(expense.note)
    }

    private fun getExpenseBinding(): Expense = binding.addLayout.let {
        val title = it.addTitle.text.toString()
        val amount = parseDouble(it.addAmount.text.toString())
        val type = it.addType.text.toString()
        val tag = it.addTag.text.toString()
        val date = it.addDate.text.toString()
        val desc = it.addDesc.text.toString()

        return@getExpenseBinding Expense(title, amount, type, tag, date, desc)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddBinding.inflate(inflater, container, false)


}