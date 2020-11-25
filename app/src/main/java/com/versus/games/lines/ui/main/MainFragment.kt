package com.versus.games.lines.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import com.versus.games.lines.R
import com.versus.games.lines.databinding.MainFragmentBinding
import com.versus.games.lines.views.Cell

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.model = viewModel
        prepareControls()
    }

    private fun prepareControls() {
        setGameFieldOnClick()
    }

    private fun setGameFieldOnClick() {
        for (row in 0 until binding.gameField.childCount) {
            for (col in 0 until (binding.gameField[0] as ViewGroup).childCount) {
                (binding.gameField[row] as ViewGroup)[col].setOnClickListener { v ->
                    viewModel.gameFieldOnClick(col, row)
                    (v as Cell).startJump() // TODO forward Cell to ViewModel
                }
            }
        }
    }

}