FROM cimg/android:2025.03

ARG USER=circleci
USER ${USER}
WORKDIR /home/${USER}
