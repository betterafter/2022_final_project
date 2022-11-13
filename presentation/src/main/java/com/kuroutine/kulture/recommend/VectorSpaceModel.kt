package com.kuroutine.kulture.recommend

import java.io.*
import java.text.DecimalFormat

// https://github.com/taki0112/TFIDF_Java/blob/master/TFIDF/src/TF_IDF/VectorSpaceModel.java
// TF-IDF 행렬 구하기

class VectorSpaceModel(wordListTxtFile: String?) {
    // 1. Integer: Document seq, 2. String: Term, 3. Double : frequency
    internal enum class WriteOption {
        TF, TFIDF, DF
    }

    var hash_tf: HashMap<Int, LinkedHashMap<String, Double>> = LinkedHashMap()
    var hash_df: HashMap<String, Double> = LinkedHashMap()
    var hash_tfidf: HashMap<Int, LinkedHashMap<String, Double>> = LinkedHashMap()
    var fReader: FileReader? = null
    var bReader: BufferedReader? = null

    init {
        try {
            fReader = FileReader(wordListTxtFile)
            bReader = BufferedReader(fReader)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun CalculateTFIDF() {
        try {
            var line = ""
            var numLine = 0
            while (bReader!!.readLine().also { line = it } != null) {
                val splitedLine = line.split(" ").toTypedArray()
                val newTFHash = LinkedHashMap<String, Double>()
                val newDFHash = LinkedHashMap<String, Double>()
                for (i in splitedLine.indices) {
                    // TF
                    if (newTFHash.containsKey(splitedLine[i])) newTFHash.replace(
                        splitedLine[i],
                        newTFHash[splitedLine[i]]!! + 1
                    ) else newTFHash[splitedLine[i]] =
                        1.0

                    // DF
                    if (!newDFHash.containsKey(splitedLine[i])) newDFHash[splitedLine[i]] =
                        1.0
                }
                // DF
                for (key in newDFHash.keys) {
                    if (hash_df.containsKey(key)) hash_df.replace(
                        key,
                        hash_df[key]!! + 1
                    ) else hash_df[key] =
                        1.0
                }
                newDFHash.clear()
                hash_tf[numLine] = newTFHash
                numLine++
                if (numLine % 499 == 0) {
                    println(numLine)
                }
            }
            hash_tfidf = hash_tf.clone() as HashMap<Int, LinkedHashMap<String, Double>>
            for (key in hash_tf.keys) {
                val hashmap = hash_tf[key]!!
                for (subkey in hashmap.keys) {
                    val tf = Math.log(1 + hashmap[subkey]!!)
                    val idf = Math.log(numLine / hash_df[subkey]!!)
                    hash_tfidf[key]!!.replace(subkey, tf * idf)
                }
            }
            bReader!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun Write(writefilePath: String?, option: WriteOption) {
        try {
            var hash_writer: HashMap<Int, LinkedHashMap<String, Double>>? = null
            if (option == WriteOption.TF) {
                hash_writer = hash_tf
            } else if (option == WriteOption.TFIDF) {
                hash_writer = hash_tfidf
            }
            val fWriter = FileWriter(writefilePath)
            val bWriter = BufferedWriter(fWriter)
            for (key in hash_writer!!.keys) {
                val hashmap: HashMap<String, Double> = hash_tf[key]!!
                bWriter.write(key.toString() + "\t")
                for (subkey in hashmap.keys) {
                    bWriter.write(
                        subkey + ":" + DecimalFormat("##.###").format(hashmap[subkey])
                            .toString() + " "
                    )
                }
                bWriter.write("\n")
            }
            bWriter.close()
            fWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}