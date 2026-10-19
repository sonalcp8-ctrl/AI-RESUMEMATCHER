// Optional C++ scoring module.
// Usage:  matcher "java,mysql,git" "java,oop,mysql,jdbc,git"
//         (argument 1 = user skills, argument 2 = job's required skills)
// Prints the match percentage (0-100) = matched required skills / total required skills.
#include <algorithm>
#include <cctype>
#include <cmath>
#include <iostream>
#include <set>
#include <sstream>
#include <string>
using namespace std;

static string clean(string s) {
    size_t a = s.find_first_not_of(" \t");
    size_t b = s.find_last_not_of(" \t");
    if (a == string::npos) return "";
    s = s.substr(a, b - a + 1);
    transform(s.begin(), s.end(), s.begin(), [](unsigned char c) { return tolower(c); });
    return s;
}

static set<string> split(const string &text) {
    set<string> out;
    stringstream ss(text);
    string item;
    while (getline(ss, item, ',')) {
        item = clean(item);
        if (!item.empty()) out.insert(item);
    }
    return out;
}

int main(int argc, char *argv[]) {
    if (argc < 3) { cout << 0 << endl; return 1; }
    set<string> user = split(argv[1]);
    set<string> job  = split(argv[2]);
    if (job.empty()) { cout << 0 << endl; return 0; }
    int matched = 0;
    for (const string &skill : job)
        if (user.count(skill)) matched++;
    cout << (int)round(100.0 * matched / job.size()) << endl;
    return 0;
}
