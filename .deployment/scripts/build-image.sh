#!/busybox/sh
export WORKSPACE="$(cd ../.. && pwd)"
. $WORKSPACE/.deployment/scripts/project-prop-reader.sh

MODULE_DIR="${WORKSPACE}/${1:-$(basename $(pwd))}"
CONTEXT_PATH="${MODULE_DIR}/$2"
cd "${MODULE_DIR}"

export ARTIFACT_ID="$(get_build_property build.artifact)"
export VERSION="$(get_build_property build.version)"
export DESCRIPTION="$(get_build_property build.name)"

export COMMIT_SHA="$(get_git_property git.commit.id.abbrev)"
export GIT_SERVER_URL="$(get_git_property git.remote.origin.url)"

executor --destination "$(read_img_name)" \
         --destination "$(read_img_name_latest)" \
      	 --dockerfile "${MODULE_DIR}/Dockerfile" \
         --build-arg NAME="${ARTIFACT_ID}" \
         --build-arg VERSION="${VERSION}" \
         --build-arg DESCRIPTION="${DESCRIPTION}" \
         --build-arg VCS_REF="${COMMIT_SHA}" \
         --build-arg VCS_URL="${GIT_SERVER_URL}" \
         --context "${CONTEXT_PATH}"
