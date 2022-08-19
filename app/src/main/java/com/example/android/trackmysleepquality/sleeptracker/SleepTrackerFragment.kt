/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
@AndroidEntryPoint
class SleepTrackerFragment : Fragment() {

    private lateinit var binding: FragmentSleepTrackerBinding
    private val viewModel: SleepTrackerViewModel by viewModels()
    private val adapter: SleepTrackerAdapter by lazy { SleepTrackerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSleepTrackerBinding.inflate(inflater, container, false)

        binding.sleepTrackerViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSleepNights.adapter = adapter

        initObservers()
    }

    private fun initObservers() {
        viewModel.nights.observe(viewLifecycleOwner) { nights ->
            nights?.let {
                adapter.submitList(nights)
            }
        }

        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner) { night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(it.nightId)
                )
                viewModel.doneNavigating()
            }
        }
        viewModel.cleared.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, getString(R.string.cleared_message), Snackbar.LENGTH_SHORT)
                .show()
        }
    }
}
