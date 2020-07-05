/*******************************************************************************
 * Copyright 2010 Alexandros Schillings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aws.apps.underthehood.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import aws.apps.underthehood.R
import aws.apps.underthehood.ui.dialogs.DialogFactory
import aws.apps.underthehood.ui.sharing.DataSharer
import aws.apps.underthehood.ui.sharing.ExportFileWriter
import aws.apps.underthehood.ui.sharing.SharePayload
import aws.apps.underthehood.ui.sharing.SharePayloadFactory
import uk.co.alt236.underthehood.commandrunner.CommandRunner
import uk.co.alt236.underthehood.commandrunner.model.Result

class Main : AppCompatActivity() {
    private lateinit var sharePayloadFactory: SharePayloadFactory

    private var model: MainViewModel? = null
    private var view: MainView? = null

    private var commandRunner: CommandRunner? = null
    private var sharePayload: SharePayload? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        commandRunner = CommandRunner(resources)
        sharePayloadFactory = SharePayloadFactory(resources)

        model = ViewModelProvider(this).get(MainViewModel::class.java)
        view = MainView(this)

        val nameObserver: Observer<Result> = Observer { newData ->
            view?.let { view ->
                view.clear()
                if (newData != null) {
                    removeDialogFragmentByTag(FRAGMENT_TAG_PROGRESS)
                    view.showResults(newData)
                    sharePayload = sharePayloadFactory.create(newData)
                }
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model!!.currentData!!.observe(this, nameObserver)
    }

    override fun onResume() {
        super.onResume()
        if (model?.currentData?.value?.timestamp == null) {
            refreshInfo()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * Handles item selections
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.menu_about -> {
                DialogFactory.createAboutDialog(this).show(supportFragmentManager, FRAGMENT_TAG_ABOUT)
                true
            }
            R.id.menu_share -> {
                sharePayload?.let { payload -> DataSharer(this).shareData(payload) }
                true
            }
            R.id.menu_to_sd -> {
                sharePayload?.let { payload -> ExportFileWriter(this).exportData(payload) }
                true
            }
            R.id.menu_refresh -> {
                refreshInfo()
                true
            }
            else -> false
        }
    }

    private fun refreshInfo() {
        DialogFactory.createProgressDialog().show(supportFragmentManager, FRAGMENT_TAG_PROGRESS)
        commandRunner?.runCommands(object : CommandRunner.Callback<Result> {
            override fun onCommandsCompleted(result: Result) {
                model?.currentData?.postValue(result)
            }
        })
    }

    private fun FragmentActivity.removeDialogFragmentByTag(tag: String) {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        fragment?.let {
            val dialog = it as DialogFragment
            dialog.dismiss()
        }
    }

    private companion object {
        const val FRAGMENT_TAG_PROGRESS = "PROGRESS_FRAGMENT"
        const val FRAGMENT_TAG_ABOUT = "ABOUT_FRAGMENT"
    }
}