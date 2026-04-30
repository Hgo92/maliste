# Global build args (used in FROM instructions)
ARG BASE_IMAGE=eclipse-temurin:17-jdk
ARG RUNTIME_BASE_IMAGE=eclipse-temurin:17-jre-jammy

# ----------- BUILD STAGE -----------
FROM ${BASE_IMAGE} AS builder

WORKDIR /app

# Install build-time system dependencies
ARG APT_PACKAGES="curl unzip"
RUN if [ -n "$APT_PACKAGES" ] && [ "$APT_PACKAGES" != "None" ]; then \
  apt-get update && apt-get install -y $APT_PACKAGES && rm -rf /var/lib/apt/lists/*; \
  fi

# Copy dependency files first (cache optimization)
ARG DEPENDENCY_FILES="pom.xml"
COPY pom.xml ./

# Pre-build commands (dependency installation)
ARG PREBUILD_COMMANDS=""
RUN if [ -n "$PREBUILD_COMMANDS" ]; then \
  echo "Running prebuild commands: $PREBUILD_COMMANDS"; \
  sh -lc "$PREBUILD_COMMANDS"; \
  fi

# Copy full application source
ARG ROOT_DIRECTORY=.
COPY ${ROOT_DIRECTORY} /app/


# Post-build commands (compilation/build step)
ARG POSTBUILD_COMMANDS="chmod +x mvnw && ./mvnw package -DskipTests"
RUN if [ -n "$POSTBUILD_COMMANDS" ]; then \
  echo "Running postbuild commands: $POSTBUILD_COMMANDS"; \
  sh -lc "$POSTBUILD_COMMANDS"; \
  fi

# ----------- RUNTIME STAGE -----------
FROM ${RUNTIME_BASE_IMAGE} AS runtime

WORKDIR /app

# Install minimal runtime dependencies
ARG RUNTIME_APT_PACKAGES="curl"
RUN if [ -n "$RUNTIME_APT_PACKAGES" ] && [ "$RUNTIME_APT_PACKAGES" != "None" ]; then \
  apt-get update && apt-get install -y $RUNTIME_APT_PACKAGES && rm -rf /var/lib/apt/lists/*; \
  fi

# Copy built application from builder stage
COPY --from=builder /app /app
COPY --from=builder /app/target/demo-backend-0.0.1-SNAPSHOT.jar /app/app.jar


# Runtime cleanup


ENV DEPLOY_CMD="java -jar /app/app.jar"
CMD ["/bin/sh", "-c", "$DEPLOY_CMD"]