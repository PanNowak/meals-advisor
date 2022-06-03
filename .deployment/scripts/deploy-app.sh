#!/usr/bin/env sh
export WORKSPACE="$(cd ../.. && pwd)"
#. $WORKSPACE/.deployment/scripts/project-prop-reader.sh

HELM_DIR="${WORKSPACE}/.deployment/helm"
APP_NAME="meals-advisor"

#TODO inject name of release
helm upgrade "${APP_NAME}-${NAMESPACE}" "${HELM_DIR}/${APP_NAME}" \
		--install --atomic --namespace ${NAMESPACE} \
	     	--set externalDatabase.existingConfiguration=meals-advisor-backend-config \
	     	--set externalDatabase.existingSecret=postgres-password
