package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ContextMenu
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mPlayer: MediaPlayer? = null
    private var whatPlaying: Int? = null
    var whatPlayingButton: Button? = null
    private var loop: Boolean = false
    private var handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var playIcon: Drawable
    private lateinit var pauseIcon: Drawable
    private var secs: Int? = null
    private var secs2: Int? = null
    private var musics = listOf(R.raw.music1, R.raw.music2, R.raw.music3, R.raw.music4, R.raw.music5, R.raw.music6, R.raw.music7, R.raw.music8, R.raw.music9, R.raw.music10, R.raw.music11, R.raw.music12, R.raw.music13, R.raw.music14, R.raw.music15, R.raw.music16,
            R.raw.music17, R.raw.music18, R.raw.music19, R.raw.music20, R.raw.music21, R.raw.music22, R.raw.music23, R.raw.music24, R.raw.music25, R.raw.music26, R.raw.music27, R.raw.music28,
            R.raw.music29, R.raw.music30, R.raw.music31, R.raw.music32, R.raw.music33, R.raw.music34, R.raw.music35, R.raw.music36, R.raw.music37, R.raw.music38, R.raw.music39, R.raw.music40, R.raw.music41, R.raw.music42, R.raw.music43, R.raw.music44, R.raw.music45,
            R.raw.music46, R.raw.music47, R.raw.music48, R.raw.music49, R.raw.music50, R.raw.music51, R.raw.music52, R.raw.music53, R.raw.music54, R.raw.music55, R.raw.music56, R.raw.music57,
            R.raw.music58, R.raw.music59, R.raw.music60, R.raw.music61, R.raw.music62, R.raw.music63, R.raw.music64, R.raw.music65, R.raw.music66, R.raw.music67, R.raw.music68, R.raw.music69, R.raw.music70, R.raw.music71, R.raw.music72, R.raw.music73, R.raw.music74,
            R.raw.music75, R.raw.music76, R.raw.music77, R.raw.music78, R.raw.music79, R.raw.music80, R.raw.music81, R.raw.music82, R.raw.music83, R.raw.music84, R.raw.music85, R.raw.music86, R.raw.music87, R.raw.music88, R.raw.music89, R.raw.music90, R.raw.music91,
            R.raw.music92, R.raw.music93, R.raw.music94, R.raw.music95, R.raw.music96, R.raw.music97, R.raw.music98, R.raw.music99, R.raw.music100, R.raw.music101, R.raw.music102, R.raw.music103, R.raw.music104, R.raw.music105,
            R.raw.music106, R.raw.music107, R.raw.music108, R.raw.music109, R.raw.music110, R.raw.music111, R.raw.music112, R.raw.music113, R.raw.music114, R.raw.music115, R.raw.music116, R.raw.music117, R.raw.music118
    )
    private lateinit var openApp: Intent
    private lateinit var pendingIntent: PendingIntent
    private lateinit var buttons: Array<Button>
    private lateinit var manager: NotificationManager
    private lateinit var notification: NotificationCompat.Builder
    private var backgroundLayout = R.drawable.gradient_background
    private var isAutoNext = true
    private var isVisualize = true
    private lateinit var sPref: SharedPreferences
    private lateinit var sPrefEdit: SharedPreferences.Editor

    private var isAutoNextKey = "autoNext"
    private var isVisualizeKey = "visualize"
    private var backgroundKey = "background"

    companion object {
        const val NOTIFY_ID = 1
        const val CHANNEL_ID = "CHANNEL_ID"
        const val CODE = 12
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sPref = getPreferences(MODE_PRIVATE)
        clayout.background = getDrawable(sPref.getInt(backgroundKey, R.drawable.gradient_background))
        backgroundLayout = sPref.getInt(backgroundKey, R.drawable.gradient_background)
        isAutoNext = sPref.getBoolean(isAutoNextKey, true)
        isVisualize = sPref.getBoolean(isVisualizeKey, true)

        registerForContextMenu(play)

        openApp = Intent(this, MainActivity::class.java)
        openApp.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        buttons = listOf(button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button20, button21, button22, button23, button24, button25, button26, button27,
            button28, button29, button30, button31, button32, button33, button34, button35, button36, button37, button38, button39, button40, button41, button42, button43, button44, button45, button46, button47, button48, button49, button50, button51, button52, button53, button54, button55,
            button56, button57, button58, button59, button60, button61, button62, button63, button64, button65, button66, button67, button68, button69, button70, button71, button72, button73, button74, button75, button76, button77, button78, button79, button80, button81, button82, button83,
            button84, button85, button86, button87, button88, button89, button90, button91, button92, button93, button94, button95, button96, button97, button98, button99, button100, button101, button102, button103, button104, button105, button106, button107, button108, button109, button110, button111, button112, button113, button114, button115, button116, button117, button118).toTypedArray()

        for (n in 0..buttons.size.minus(1)) { initButton(buttons[n], musics[n]) }

        notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.play)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        next.setOnClickListener { playNext() }
        past.setOnClickListener { playPast() }
        play.setOnClickListener { playButton() }
        repeat.setOnClickListener { repeat() }

        playIcon = ContextCompat.getDrawable(this, R.drawable.play_icon)!!
        pauseIcon = ContextCompat.getDrawable(this, R.drawable.pause_icon)!!

        seekBar.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (mPlayer != null && fromUser) {
                    mPlayer?.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("background", backgroundLayout)
        intent.putExtra("isAutoNext", isAutoNext)
        intent.putExtra("isVisualize", isVisualize)
        startActivityForResult(intent, CODE)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (data.extras?.getInt("background") != 0) clayout.background = data.extras?.let { getDrawable(it.getInt("background")) }
        if (data.extras?.getInt("background") != 0) backgroundLayout = data.extras?.getInt("background")!!
        isAutoNext = data.extras?.getBoolean("isAutoNext") == true
        isVisualize = data.extras?.getBoolean("isVisualize") == true

        sPref = getPreferences(MODE_PRIVATE)
        sPrefEdit = sPref.edit()
        sPrefEdit.putInt(backgroundKey, backgroundLayout)
        sPrefEdit.putBoolean(isAutoNextKey, isAutoNext)
        sPrefEdit.putBoolean(isVisualizeKey, isVisualize)
        sPrefEdit.apply()
    }

    private fun createChannelIfNeeded(manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT))
        }
    }

    override fun onDestroy() {
        manager.cancelAll()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun playNext() {
        if (mPlayer == null) Toast.makeText(applicationContext, "Выберите песню!", Toast.LENGTH_SHORT).show()
        else if (mPlayer != null && whatPlayingButton == buttons[buttons.size - 1]) playMusic(buttons[0], musics[0])
        else {
            playMusic(buttons[(buttons.indexOf(whatPlayingButton)).plus(1)], musics[(musics.indexOf(whatPlaying)).plus(1)])
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun playPast() {
        if (mPlayer == null) Toast.makeText(applicationContext, "Выберите песню!", Toast.LENGTH_SHORT).show()
        else if (mPlayer != null &&whatPlayingButton == buttons[0]) playMusic(buttons[buttons.size - 1], musics[musics.size - 1])
        else {
            playMusic(buttons[(buttons.indexOf(whatPlayingButton)).minus(1)], musics[(musics.indexOf(whatPlaying)).minus(1)])
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun playButton() {
        if (mPlayer != null && mPlayer?.isPlaying!!) {
            mPlayer?.pause()
            play.setImageResource(R.drawable.play)
        }

        else if (mPlayer != null && !mPlayer?.isPlaying!!) {
            mPlayer?.start()
            play.setImageResource(R.drawable.pause)
        }

        if (mPlayer == null) {
            playMusic(buttons[0], musics[0])
            play.setImageResource(R.drawable.pause)
        }
    }

    private fun repeat() {
        if (mPlayer != null && !mPlayer?.isLooping!!) {
            mPlayer?.isLooping = true
            repeat.setImageResource(R.drawable.loop)
        }
        else if (mPlayer != null && mPlayer?.isLooping!!) {
            mPlayer?.isLooping = false
            repeat.setImageResource(R.drawable.repeat)
        }
        else if (mPlayer == null) Toast.makeText(applicationContext, "Выберите песню!", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun playMusic(button: Button, music: Int) {
        if (mPlayer != null && (mPlayer?.duration!! == MediaPlayer.create(this, music).duration)) {
            if (mPlayer?.isPlaying!!) {
                mPlayer?.pause()
                whatPlayingButton?.setCompoundDrawablesWithIntrinsicBounds(playIcon, null, null, null)
            }
            else if (!mPlayer?.isPlaying!!) {
                mPlayer?.start()
                button.setCompoundDrawablesWithIntrinsicBounds(pauseIcon, null, null, null)
            }
        }

        else {
            if (mPlayer != null && mPlayer?.isPlaying!!) {
                mPlayer?.stop()
                mPlayer?.prepare()
            }

            if (whatPlayingButton != null && whatPlayingButton != button) {
                whatPlayingButton?.setCompoundDrawablesWithIntrinsicBounds(playIcon, null, null, null)
            }
            if (mPlayer != null && mPlayer?.isLooping!!) loop = true
            else if (mPlayer != null && !mPlayer?.isLooping!!) loop = false
            whatPlayingButton = button
            mPlayer = MediaPlayer.create(this, music)
            button.setCompoundDrawablesWithIntrinsicBounds(pauseIcon, null, null, null)
            whatPlaying = music
            mPlayer?.start()
            notification.setContentText("Сейчас играет: ${whatPlayingButton?.text}")
            createChannelIfNeeded(manager)
            manager.notify(NOTIFY_ID, notification.build())
            name.text = whatPlayingButton?.text
            if (name.length() in 22..30) name.textSize = 20F
            else if (name.length() > 30) name.textSize = 17F
            if (name.text == R.string.music73.toString()) name.textSize = 12F
            else if (name.text == R.string.music25.toString()) name.textSize = 16F
            if (loop) mPlayer?.isLooping = true
            secs2 = mPlayer?.duration!! / 1000
            duration.text = "%d:%02d".format(secs2!! / 60, secs2!! % 60)
            seekBar.max = mPlayer?.duration!!

            if (isVisualize) {
                val sessionId = mPlayer?.audioSessionId
                if (sessionId != -1) visualizerMusic.setAudioSessionId(sessionId!!)
            }

            handlerFunctions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initButton(button: Button, music: Int) { button.setOnClickListener { playMusic(button, music) } }

    private fun handlerFunctions() {
        handler.post(object : Runnable {
            @SuppressLint("SetTextI18n", "WrongConstant")
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun run() {
                seekBar.progress = mPlayer?.currentPosition!!
                secs = mPlayer?.currentPosition!! / 1000
                currentPos.text = "%d:%02d".format(secs!! / 60, secs!! % 60)
                if (mPlayer?.duration!! == mPlayer?.currentPosition!!) mPlayer?.stop()
                if (( isAutoNext && (!mPlayer?.isPlaying!! && !mPlayer?.isLooping!!)) && (seekBar.progress == seekBar.max)) {
                    playNext()
                }

                if (mPlayer?.isPlaying!!) {
                    whatPlayingButton?.setCompoundDrawablesWithIntrinsicBounds(pauseIcon, null, null, null)
                    play.setImageResource(R.drawable.pause)
                }
                else if(!mPlayer?.isPlaying!!) {
                    whatPlayingButton?.setCompoundDrawablesWithIntrinsicBounds(playIcon, null, null, null)
                    play.setImageResource(R.drawable.play)
                }

                if (mPlayer?.isLooping!!) repeat.setImageResource(R.drawable.loop)
                else if (!mPlayer?.isLooping!!) repeat.setImageResource(R.drawable.repeat)

                if (!isVisualize) visualizerMusic.visibility = -1
                else visualizerMusic.visibility = 0

                handler.postDelayed(this, 1)
            }
        })
    }
}