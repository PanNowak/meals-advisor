#project-prop-reader
read_img_name() {
    ARTIFACT_ID="${ARTIFACT_ID:-$(get_build_property build.artifact)}"
    VERSION="${VERSION:-$(get_build_property build.version)}"
    COMMIT_SHA="${COMMIT_SHA:-$(get_git_property git.commit.id.abbrev)}"

    echo "pannowak/${ARTIFACT_ID}:${VERSION}-${COMMIT_SHA}"
}

read_img_name_latest() {
      ARTIFACT_ID="${ARTIFACT_ID:-$(get_build_property build.artifact)}"
      echo "pannowak/${ARTIFACT_ID}:latest"
}

get_build_property() {
    PROP_KEY=$1
    echo $(get_property "$PROP_KEY" "target/classes/META-INF/build-info.properties")
}

get_git_property() {
    PROP_KEY=$1
    echo $(get_property "$PROP_KEY" "target/classes/META-INF/git.properties")
}

get_property() {
   PROP_KEY=$1
   PROP_FILE=$2
   echo $(cat "$PROP_FILE" | grep "$PROP_KEY" | cut -d'=' -f2)
}	
