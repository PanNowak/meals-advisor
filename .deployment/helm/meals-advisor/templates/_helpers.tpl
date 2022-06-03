{{/*
Expand the name of the chart.
*/}}
{{- define "meals-advisor.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{- define "meals-advisor.backend.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 55 | trimSuffix "-" | printf "%s-backend" }}
{{- end }}

{{- define "meals-advisor.frontend.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 54 | trimSuffix "-" | printf "%s-frontend" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "meals-advisor.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "meals-advisor.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "meals-advisor.common.labels" -}}
helm.sh/chart: {{ include "meals-advisor.chart" . }}
{{ include "meals-advisor.common.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "meals-advisor.common.selectorLabels" -}}
app.kubernetes.io/name: {{ include "meals-advisor.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "meals-advisor.backend.labels" -}}
{{ include "meals-advisor.common.labels" . }}
app.kubernetes.io/component: backend
{{- end }}

{{- define "meals-advisor.backend.selectorLabels" -}}
{{ include "meals-advisor.common.selectorLabels" . }}
app.kubernetes.io/component: backend
{{- end }}

{{- define "meals-advisor.frontend.labels" -}}
{{ include "meals-advisor.common.labels" . }}
app.kubernetes.io/component: frontend 
{{- end }}

{{- define "meals-advisor.frontend.selectorLabels" -}}
{{ include "meals-advisor.common.selectorLabels" . }}
app.kubernetes.io/component: frontend
{{- end }}

{{- define "meals-advisor.backend.configName" -}}
{{- .Values.externalDatabase.existingConfiguration | default (printf "%s-config" (include "meals-advisor.backend.name" .)) }}
{{- end }}

{{- define "meals-advisor.backend.secretName" -}}
{{- .Values.externalDatabase.existingSecret | default (printf "%s-secret" (include "meals-advisor.backend.name" .)) }}
{{- end }}
