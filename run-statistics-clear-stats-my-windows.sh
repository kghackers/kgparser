echo "Environment variables values: "
echo "ROOT_WORKING_DIR: $ROOT_WORKING_DIR"

if [ -z "$ROOT_WORKING_DIR" ]; then
    echo "ROOT_WORKING_DIR not defined. Do nothing."
    return 1
fi

GENERATE_STATISTICS_DIRECTORY_NAME=/stats

# realpath prevents double slashes
GENERATE_STATISTICS_DIRECTORY_PATH=$(realpath "$ROOT_WORKING_DIR/$GENERATE_STATISTICS_DIRECTORY_NAME")

echo "Stats directory: $GENERATE_STATISTICS_DIRECTORY_PATH"

echo "Removing everything from stats directory $GENERATE_STATISTICS_DIRECTORY_PATH..."

# if we quote, it does NOT work
rm -rf $GENERATE_STATISTICS_DIRECTORY_PATH/*

echo "Removed everything from stats directory $GENERATE_STATISTICS_DIRECTORY_PATH."
