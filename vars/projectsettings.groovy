@groovy.transform.Field
def buildNumber = ""

@groovy.transform.Field
def version = ""

def increaseAndroidBuildNumber(env) {
	filename = env.workspace + '/ProjectSettings/ProjectSettings.asset'

	// read all the lines into a list, each line is an element in the list
	File fh = new File(filename)
	def lines = fh.readLines()
	lines.eachWithIndex{ it, i -> 
		def tag = "AndroidBundleVersionCode: "
		if(it.indexOf(tag) != -1) {
			def split1 = it.split(tag)
			buildNumber = split1[1]
			def newVersion = Integer.parseInt(split1[1]) + 1
			lines[i] = split1[0] + tag + newVersion
		}
	}

	fh.withWriter('utf8') {
		writer -> lines.each { line -> 
			println(line)
			writer.writeLine(line)
		}
	}
}

def increaseBundlePatchVersion(env) {
	filename = env.workspace + '/ProjectSettings/ProjectSettings.asset'

	// read all the lines into a list, each line is an element in the list
	File fh = new File(filename)
	def lines = fh.readLines()
	lines.eachWithIndex{ it, i -> 
		def tag2 = "bundleVersion: "
		if(it.indexOf(tag2) != -1) {
			def split2 = it.split(tag2)
			version = split2[1]
			def versionSplit = split2[1].split("\\.")
			if(versionSplit.size() >= 3) {
				def patchVersion = Integer.parseInt(versionSplit[2]) + 1
				versionSplit[2] = patchVersion
			}
			lines[i] = split2[0] + tag2
			for(i2 in 0 .. versionSplit.size()-1) {
				lines[i] += versionSplit[i2]
				if(i2 < versionSplit.size() - 1) {
					lines[i] += "."
				}
			}
		}
	}

	fh.withWriter('utf8') {
		writer -> lines.each { line -> 
			println(line)
			writer.writeLine(line)
		}
	}
}