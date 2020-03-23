package me.sedlar.midi

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import me.sedlar.midi.binding.MIDIBinding
import java.io.File
import java.nio.file.Files
import java.util.HashMap
import java.util.ArrayList



object ProfileManager {

    val DIRECTORY = File(System.getProperty("user.home") + File.separator + "MidiMapper" + File.separator + "Profiles" + File.separator)
    val PROFILES = HashMap<String, ArrayList<DeviceProfile>>()

    fun createOrGetProfile(deviceName: String, profileName: String?): DeviceProfile? {
        if (profileName == null) return null
        var result: DeviceProfile? = null
        if (PROFILES.containsKey(deviceName)) {
            val profiles = PROFILES[deviceName]!!
            for (profile in profiles) {
                if (profile.name == profileName) {
                    result = profile
                }
            }
        }
        if (result == null) {
            if (!PROFILES.containsKey(deviceName)) {
                PROFILES[deviceName] = ArrayList()
            }
            result = DeviceProfile(deviceName, profileName)
            PROFILES[deviceName]!!.add(result)
        }
        saveProfile(result)
        return result
    }

    fun saveProfile(profile: DeviceProfile) {
        if (!PROFILES.containsKey(profile.deviceName)) {
            PROFILES[profile.deviceName] = ArrayList()
        }
        if (!PROFILES[profile.deviceName]!!.contains(profile)) {
            PROFILES[profile.deviceName]!!.add(profile)
        }
        val targetFile = profile.location
        targetFile.parentFile.mkdirs()
        targetFile.createNewFile()
        val dataMap = HashMap<String, Any>()
        dataMap["deviceName"] = profile.deviceName
        dataMap["name"] = profile.name
        dataMap["bindings"] = profile.bindings.map {
            mapOf(
                "btn" to it.btn,
                "trigger" to it.trigger,
                "type" to it.type,
                "output" to it.output,
                "data" to it.data,
                "args" to it.args
            )
        }
        val json = GsonBuilder().setPrettyPrinting().create().toJson(dataMap)
        Files.write(targetFile.toPath(), json.toByteArray())
    }

    fun deleteProfile(profile: DeviceProfile): Int? {
        profile.location.delete()
        if (PROFILES.containsKey(profile.deviceName)) {
            val idx= PROFILES[profile.deviceName]!!.indexOf(profile)
            PROFILES[profile.deviceName]!!.remove(profile)
            return idx
        }
        return null
    }

    fun loadProfiles(deviceName: String): List<DeviceProfile> {
        val list = ArrayList<DeviceProfile>()
        val dir = File(DIRECTORY, deviceName + File.separator)
        dir.listFiles()?.forEach { file ->
            if (file.name.endsWith(".json")) {
                list.add(loadFromFile(file))
            }
        }
        for (profile in list) {
            if (!PROFILES.containsKey(profile.deviceName)) {
                PROFILES[deviceName] = ArrayList()
            }
            if (PROFILES[deviceName]!!.find { it.name == profile.name } == null) {
                PROFILES[deviceName]!!.add(profile)
            }
        }
        return list
    }

    @Suppress("UNCHECKED_CAST")
    fun loadFromFile(file: File): DeviceProfile {
        val jsonText = String(Files.readAllBytes(file.toPath()))
        val json: HashMap<String, Any> = Gson().fromJson(jsonText, HashMap::class.java) as HashMap<String, Any>
        println(json)
        val deviceName: String = json["deviceName"].toString()
        val profileName: String = json["name"].toString()
        val profile = DeviceProfile(deviceName, profileName)
        for (binding in json["bindings"] as List<LinkedTreeMap<String, Any>>) {
            profile.bindings.add(MIDIBinding(
                btn = binding["btn"].toString().toDouble().toByte(),
                trigger = binding["trigger"].toString(),
                type = binding["type"].toString(),
                output = binding["output"].toString(),
                data = binding["data"].toString(),
                args = (binding["args"] as ArrayList<Any>).map { it.toString() }.toTypedArray()
            ))
        }
        return profile
    }
}