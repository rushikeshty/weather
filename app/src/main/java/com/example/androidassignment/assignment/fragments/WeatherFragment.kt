package com.example.androidassignment.assignment.fragments

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.androidassignment.R
import com.example.androidassignment.assignment.PermissionListener
import com.example.androidassignment.assignment.firebase.FirebaseAuthentication
import com.example.androidassignment.assignment.networkservices.NetworkChangeReceiver
import com.example.androidassignment.assignment.notification.SendNotification
import com.example.androidassignment.assignment.viewmodel.WeatherViewModel
import com.example.androidassignment.databinding.FragmentWeatherBinding
import com.google.android.material.snackbar.Snackbar


class WeatherFragment : Fragment(R.layout.fragment_weather), PopupMenu.OnMenuItemClickListener,
    NetworkChangeReceiver.ConnectivityReceiverListener {

    private var notistring = ""
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var weatherBinding: FragmentWeatherBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private var cityName: String = ""
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private lateinit var sendNotification: SendNotification
    private var grant: Boolean = false
    private lateinit var firebaseAuthentication: FirebaseAuthentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            grant = isGranted

            sendNotificationAfterPermission()
        }

        networkChangeReceiver = NetworkChangeReceiver()
        sendNotification = SendNotification(requireContext())
        firebaseAuthentication = FirebaseAuthentication()


        //show action bar cast fragment activity to AppCombat activity
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.welcome_rushikesh)
        actionBar?.show()
        val intentFilter = IntentFilter()
        intentFilter.addAction(getString(R.string.android_net_conn_connectivity_change))
        requireContext().registerReceiver(networkChangeReceiver, intentFilter)

    }


    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(networkChangeReceiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        weatherBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        return weatherBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (::weatherBinding.isInitialized) {
            setToLoWAlphaForViews()
            val imageview = view.findViewById<ImageView>(R.id.imageview)
            Glide.with(requireContext()).load(R.drawable.weathergif).into(imageview)

            weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
            weatherBinding.viewModel = weatherViewModel
            weatherBinding.lifecycleOwner = this

            weatherBinding.temperature.setOnClickListener {
                val popupMenu = PopupMenu(context, it)
                popupMenu.setOnMenuItemClickListener(this)
                popupMenu.inflate(R.menu.menu)
                popupMenu.show()
            }

            if (::weatherViewModel.isInitialized) {
                weatherViewModel.getDataFromAPI("pune")

            }

            val cities = arrayOf("satara", "mumbai", "pune", "kolhapur")

            val adapter = activity?.let { it1 ->
                ArrayAdapter(it1.applicationContext, android.R.layout.select_dialog_item, cities)
            }

            weatherBinding.searchbar.threshold = 1
            weatherBinding.searchbar.setAdapter(adapter)

            weatherBinding.searchbar.setOnItemClickListener { _, _, i, _ ->
                hideKeyBoard()
                val item = adapter?.getItem(i).toString()
                if (item.isNotBlank()) {
                    cityName = item
                    weatherViewModel.getDataFromAPI(cityName)
                    weatherBinding.progressbar.visibility = View.VISIBLE
                    setToLoWAlphaForViews()

                }
            }


            weatherBinding.searchbutton.setOnClickListener {
                hideKeyBoard()

                if (!weatherBinding.searchbar.text.isNullOrBlank()) {
                    cityName = weatherBinding.searchbar.text.toString()
                    weatherViewModel.getDataFromAPI(cityName)
                    weatherBinding.progressbar.visibility = View.VISIBLE
                    setToLoWAlphaForViews()
                } else {
                    Toast.makeText(requireContext(), "Enter city name", Toast.LENGTH_SHORT).show()
                }
            }

            weatherBinding.swipeRefresh.setColorSchemeResources(
                R.color.red, R.color.green, R.color.blue, R.color.yellow
            )

            weatherBinding.swipeRefresh.setOnRefreshListener {

                if (!weatherBinding.searchbar.text.isNullOrBlank()) {
                    weatherViewModel.getDataFromAPI(cityName)
                } else {
                    weatherViewModel.getDataFromAPI("pune")
                }

                weatherBinding.progressbar.visibility = View.VISIBLE
                setToLoWAlphaForViews()
            }

            weatherBinding.searchbar.apply {
                hint = "Enter city name..."

            }




            weatherViewModel.livedata.observe(viewLifecycleOwner) {
                weatherBinding.progressbar.visibility = View.INVISIBLE
                setToHighAlphaForViews()
                weatherBinding.swipeRefresh.isRefreshing = false
                // Toast.makeText(requireContext(), grant.toString(), Toast.LENGTH_SHORT).show()
                if (::sendNotification.isInitialized && grant) {
                    Log.d("ABCDEFGHI", it.weathererror)
                    if (it.weathererror.isNotEmpty()) {
                        sendNotification.sendWeatherNotificationToUser(it.weathererror)
                        if(it.weathererror!="working"){
                            weatherBinding.foundStatus.apply {
                                visibility = View.VISIBLE
                                text = it.weathererror
                            }
                        }

                    } else {
                         weatherBinding.foundStatus.visibility = View.GONE
                        sendNotification.sendWeatherNotificationToUser(it.location + "  " + it.weathercondition + " temp " + it.temp)
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

        }

        weatherBinding.logout.setOnClickListener {
            showAlertDialog()

        }

    }

    private fun showAlertDialog() {
        val diaLog = AlertDialog.Builder(requireContext())
        diaLog.apply {
            setTitle("Alert")
            setIcon(R.drawable.alert)
            setMessage("Are you sure want to log out")
        }

        diaLog.setPositiveButton(
            "Yes",
            DialogInterface.OnClickListener(object : (DialogInterface, Int) -> Unit {

                override fun invoke(p1: DialogInterface, p2: Int) {
                    firebaseAuthentication.signOut()
                    findNavController().navigate(R.id.action_weather_to_loginFragment)
                }

            })
        ).setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener(object : (DialogInterface, Int) -> Unit {
                override fun invoke(p1: DialogInterface, p2: Int) {
                    p1.dismiss()
                }

            })
        ).create()
        diaLog.show()
    }

    private fun setToLoWAlphaForViews() {
        weatherBinding.imageview.alpha = 0.2f
        weatherBinding.location.alpha = 0.2f
        weatherBinding.state.alpha = 0.2f
        weatherBinding.country.alpha = 0.2f
        weatherBinding.linearlayout2.alpha = 0.2f
        weatherBinding.weathercondition.alpha = 0.2f
        weatherBinding.weatherimageset.alpha = 0.2f
        weatherBinding.datetime.alpha = 0.2f
        weatherBinding.temperature.alpha = 0.2f
    }

    private fun setToHighAlphaForViews() {
        weatherBinding.imageview.alpha = 1f
        weatherBinding.location.alpha = 1f
        weatherBinding.state.alpha = 1f
        weatherBinding.country.alpha = 1f
        weatherBinding.linearlayout2.alpha = 1f
        weatherBinding.weathercondition.alpha = 1f
        weatherBinding.weatherimageset.alpha = 1f
        weatherBinding.datetime.alpha = 1f
        weatherBinding.temperature.alpha = 1f
        weatherBinding.temperature.paintFlags =
            weatherBinding.temperature.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.Celsius -> weatherBinding.temperature.text = weatherViewModel.tempC
            R.id.Fahrenheit -> weatherBinding.temperature.text = weatherViewModel.tempf

        }
        return true
    }


    private fun hideKeyBoard() {

        val keyboard =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.

        var view = requireActivity().currentFocus

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(requireContext())
        }

        keyboard.hideSoftInputFromWindow(view.windowToken, 0)


    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            Snackbar.make(
                weatherBinding.root, "Fetching data", Snackbar.LENGTH_SHORT
            ).apply {
                animationMode = Snackbar.ANIMATION_MODE_FADE
                setTextColor(Color.BLACK)
                setBackgroundTint(requireContext().getColor(R.color.green))
                show()
            }

            weatherBinding.progressbar.visibility = View.VISIBLE
            setToLoWAlphaForViews()
            weatherViewModel.getDataFromAPI("pune")
        } else {
            Snackbar.make(
                weatherBinding.root,
                "check your internet connection and try again",
                Snackbar.LENGTH_LONG
            ).show()
            if (weatherBinding.swipeRefresh.isRefreshing) {
                Snackbar.make(
                    weatherBinding.root,
                    "check your internet connection and try again",
                    Snackbar.LENGTH_LONG
                ).show()
            }


        }

    }

    private fun checkNotificationPermission(): Boolean {
        if (requireActivity().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requireActivity().requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
                )

            }
            return false
        } else {
            return true
        }
    }

    private fun sendNotificationAfterPermission() {
        if (weatherBinding.location.text.toString() != "") {
            notistring =
                weatherBinding.location.text.toString() + " " + weatherBinding.weathercondition.text.toString() + " temp " + weatherBinding.temperature.text.toString()

            sendNotification.sendWeatherNotificationToUser(notistring)
        }

    }



}