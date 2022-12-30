package com.ivos.bcash.view.stats

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ivos.bcash.R
import com.ivos.bcash.databinding.FragmentStatsBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.ivos.bcash.data.model.Expense
import com.ivos.bcash.view.base.BaseFragment
import com.ivos.bcash.view.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : BaseFragment<FragmentStatsBinding, BaseViewModel>() {

    override val viewModel: BaseViewModel by activityViewModels()

    private lateinit var pieChart: PieChart

    private var dataList = emptyList<Expense>()

    private val listOfSums = mutableListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        dataList = viewModel.list
        fillSums()
        setupPieChart()
        loadPieChartData()
        setupCardExpanses()
    }

    override fun onPause() {
        super.onPause()
        if (dataList.isEmpty()) {
            pieChart.clear()
        }
    }

    private fun initializeView() {
        pieChart = binding.pieChart
    }

    private fun fillSums() {
        for (expanse in dataList) {
            if(expanse.type == requireContext().getString(R.string.income)) {
                listOfSums[8] += expanse.amount
            } else {
                when (expanse.tag) {
                    requireContext().getString(R.string.housing) -> listOfSums[0] += expanse.amount
                    requireContext().getString(R.string.transportation) -> listOfSums[1] += expanse.amount
                    requireContext().getString(R.string.food) -> listOfSums[2] += expanse.amount
                    requireContext().getString(R.string.utilities) -> listOfSums[3] += expanse.amount
                    requireContext().getString(R.string.insurance) -> listOfSums[4] += expanse.amount
                    requireContext().getString(R.string.healthcare) -> listOfSums[5] += expanse.amount
                    requireContext().getString(R.string.entertainment) -> listOfSums[7] += expanse.amount
                    else -> listOfSums[6] += expanse.amount
                }
            }

        }
    }

    private fun setupPieChart() {
        with(pieChart) {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(16f)
            setEntryLabelColor(Color.BLACK)
            centerText = requireContext().getString(R.string.spending)
            setCenterTextSize(26f)
            description.isEnabled = false
        }

        val leg = pieChart.legend
        with(leg) {
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            textSize = 16f
            setDrawInside(false)
            isEnabled = true
        }
    }

    private fun loadPieChartData() {

        val listOfExpanses = mutableMapOf<Float, String>()
        val listOfColors = linkedMapOf<String, Int>()

        listOfSums.forEachIndexed { index, d ->
            if (d != 0.0) {
                val name = when (index) {
                    0 -> requireContext().getString(R.string.housing)
                    1 -> requireContext().getString(R.string.transportation)
                    2 -> requireContext().getString(R.string.food)
                    3 -> requireContext().getString(R.string.utilities)
                    4 -> requireContext().getString(R.string.insurance)
                    5 -> requireContext().getString(R.string.healthcare)
                    6 -> requireContext().getString(R.string.miscellaneous)
                    7 -> requireContext().getString(R.string.entertainment)
                    else -> requireContext().getString(R.string.income)
                }
                listOfExpanses[d.toFloat()] = name

                val color = when (index) {
                    0 -> Color.rgb(228, 65, 43)
                    1 -> Color.rgb(141, 153, 174)
                    2 -> Color.rgb(0, 8, 209)
                    3 -> Color.rgb(0, 159, 249)
                    4 -> Color.rgb(255, 122, 0)
                    5 -> Color.rgb(228, 43, 121)
                    6 -> Color.rgb(187, 134, 252)
                    7 -> Color.rgb(249, 0, 239)
                    else -> Color.rgb(0, 171, 115)
                }

                listOfColors[name] = color
            }
        }

        val listPieEntries = mutableListOf<PieEntry>()

        listOfExpanses.forEach { (k, v) ->
            listPieEntries.add(PieEntry(k, v))
        }

        val colors = listOfColors.values.toList()

        val dataSet = PieDataSet(listPieEntries, "")

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(16f)
        data.setValueTextColor(Color.BLACK)

        pieChart.data = data
        pieChart.invalidate()
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun setupCardExpanses() {

        var sum = 0.0

        listOfSums.forEach {
            sum += it
        }

        if (sum != 0.0) {
            binding.cvExpanses.visibility = View.VISIBLE
        } else {
            binding.cvExpanses.visibility = View.GONE
            pieChart.visibility = View.GONE
            binding.ivEmptyStats.visibility = View.VISIBLE
            binding.tvEmptyStats.visibility = View.VISIBLE
        }

        if (listOfSums[0] != 0.0) {
            binding.llHousing.visibility = View.VISIBLE
            binding.tvHousingSum.text = listOfSums[0].toInt().toString()
        }
        if (listOfSums[1] != 0.0) {
            binding.llTransportation.visibility = View.VISIBLE
            binding.tvTransportationSum.text = listOfSums[1].toInt().toString()
        }
        if (listOfSums[2] != 0.0) {
            binding.llFood.visibility = View.VISIBLE
            binding.tvFoodSum.text = listOfSums[2].toInt().toString()
        }
        if (listOfSums[3] != 0.0) {
            binding.llUtilities.visibility = View.VISIBLE
            binding.tvUtilitiesSum.text = listOfSums[3].toInt().toString()
        }
        if (listOfSums[4] != 0.0) {
            binding.llInsurance.visibility = View.VISIBLE
            binding.tvInsuranceSum.text = listOfSums[4].toInt().toString()
        }
        if (listOfSums[5] != 0.0) {
            binding.llHealthcare.visibility = View.VISIBLE
            binding.tvHealthcareSum.text = listOfSums[5].toInt().toString()
        }
        if (listOfSums[6] != 0.0) {
            binding.llOther.visibility = View.VISIBLE
            binding.tvOtherSum.text = listOfSums[6].toInt().toString()
        }
        if (listOfSums[7] != 0.0) {
            binding.llEntertainment.visibility = View.VISIBLE
            binding.tvEntertainmentSum.text = listOfSums[7].toInt().toString()
        }
        if (listOfSums[8] != 0.0) {
            binding.llIncome.visibility = View.VISIBLE
            binding.tvIncomeSum.text = listOfSums[8].toInt().toString()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentStatsBinding.inflate(
        inflater, container, false
    )
}