package com.digitalhouse.desafiofirebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.digitalhouse.desafiofirebase.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load(args.game.img).fit().centerCrop().into(binding.ivThumb)

        binding.tvTitle.text = args.game.name
        binding.tvTitle2.text = args.game.name
        binding.tvLancamento.text = args.game.year
        binding.tvDescription.text = args.game.description

        binding.tbDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.fbEdit.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_registerGameFragment)

        }

    }

}