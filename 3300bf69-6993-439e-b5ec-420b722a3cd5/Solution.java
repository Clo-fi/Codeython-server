class Solution {
    public int[] solution(int N, int[] values) {
        int[] answer = new int[values.length];
        for (int i = 0; i < N; i++) {
            answer[i] = values[i] * 2;
        }
        return answer;
    }
}
