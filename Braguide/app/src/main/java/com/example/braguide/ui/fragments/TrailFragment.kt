package com.example.braguide.ui.fragments


import androidx.fragment.app.Fragment


class TrailFragment : Fragment() {/*
    private var trailViewModel: TrailsViewModel? = null
    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = requireArguments().getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_trail_description, container, false)
        val viewModelFactory = TrailsViewModelFactory(requireActivity().application)
        trailViewModel = ViewModelProvider(this, viewModelFactory)[TrailsViewModel::class.java]

        trailViewModel!!.getTrailById(id).observe(viewLifecycleOwner) { x -> loadView(view, x)}

        return view
    }

    private fun loadView(view: View, trail: Trail) {
        val title = view.findViewById<TextView>(R.id.trail_description_title)
        title.text = trail.trailName
        val image = view.findViewById<ImageView>(R.id.trailImageDescription)
        Picasso.get().load(
            trail.imageUrl
                .replace("http", "https")
        )
            .into(image)
        val intro = view.findViewById<Button>(R.id.start_trip_button)
        intro.setOnClickListener { v: View? ->
            if (NotificationManagerCompat.from(requireContext())
                    .areNotificationsEnabled()
            ) {
                val intent: Intent = Intent(
                    requireActivity(),
                    SearchActivity::class.java
                )
                intent.putExtra("id", trail.id) // add a string extra
                startActivity(intent)
            } else {
                // Create an intent to open app notification settings
                val settingsIntent = Intent()
                settingsIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                settingsIntent.putExtra(
                    Settings.EXTRA_APP_PACKAGE,
                    requireContext().packageName
                )

                // Check if the intent can be resolved
                if (settingsIntent.resolveActivity(
                        requireContext().packageManager
                    ) != null
                ) {
                    startActivity(settingsIntent)
                } else {
                    // Provide an alternative action or message if the intent cannot be resolved
                    Toast.makeText(
                        requireContext(),
                        "Please enable notifications in your device settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }*/
}