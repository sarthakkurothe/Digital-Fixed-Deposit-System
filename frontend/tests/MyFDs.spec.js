import { describe, it, expect, beforeEach, vi } from "vitest";
import { shallowMount, flushPromises } from "@vue/test-utils";
import MyFDs from "../src/views/MyFDs.vue";

// mock toastification
vi.mock("vue-toastification", () => ({
  useToast: () => ({
    success: vi.fn(),
    error: vi.fn(),
  }),
}));

// mock clipboard
Object.assign(navigator, {
  clipboard: {
    writeText: vi.fn().mockResolvedValue(),
  },
});

describe("MyFDs.vue", () => {
  let wrapper;
  let store;

  const sampleFDs = [
    { id: 1, amount: 50000, interest_rate: 6.5, status: "ACTIVE", tenure_months: 12, maturity_date: "2025-12-01", start_date: "2024-12-01", accrued_interest: 2000 },
    { id: 2, amount: 25000, interest_rate: 7.2, status: "MATURED", tenure_months: 6, maturity_date: "2024-06-01", start_date: "2023-12-01", accrued_interest: 1000 },
    { id: 3, amount: 75000, interest_rate: 6.8, status: "ACTIVE", tenure_months: 24, maturity_date: "2026-12-01", start_date: "2024-12-01", accrued_interest: 5000 },
  ];

  beforeEach(() => {
    store = {
      getters: { getFDs: sampleFDs },
      dispatch: vi.fn(),
      state: { fds: sampleFDs, loading: false },
    };

    wrapper = shallowMount(MyFDs, {
      global: {
        mocks: {
          $store: store,
        },
      },
    });
  });

  it("renders loading state", async () => {
    wrapper = shallowMount(MyFDs, {
      global: { mocks: { $store: { state: { fds: [], loading: true }, getters: { getFDs: [] }, dispatch: vi.fn() } } },
    });
    expect(wrapper.text()).toContain("Loading FDs...");
  });

  it("shows summary cards with correct counts", () => {
    expect(wrapper.text()).toContain("Total FDs");
    expect(wrapper.text()).toContain(sampleFDs.length.toString());
    expect(wrapper.vm.filteredActiveCount).toBe(2);
    expect(wrapper.vm.filteredMaturedCount).toBe(1);
    expect(wrapper.vm.filteredTotalValue).toBe(150000);
  });

  it("filters by search term", async () => {
    await wrapper.setData({ filters: { ...wrapper.vm.filters, search: "75000" } });
    expect(wrapper.vm.filteredFds).toHaveLength(1);
    expect(wrapper.vm.filteredFds[0].id).toBe(3);
  });

  it("filters by status", async () => {
    await wrapper.setData({ filters: { ...wrapper.vm.filters, status: "MATURED" } });
    expect(wrapper.vm.filteredFds).toHaveLength(1);
    expect(wrapper.vm.filteredFds[0].status).toBe("MATURED");
  });

  it("applies quick filter: high_value", async () => {
    wrapper.vm.applyQuickFilter("high_value");
    expect(wrapper.vm.filters.amountMin).toBeGreaterThan(0);
  });

  it("opens and closes FD detail modal", async () => {
    wrapper.vm.openViewModal(sampleFDs[0]);
    expect(wrapper.vm.showModal).toBe(true);
    expect(wrapper.vm.selectedFD.id).toBe(1);

    wrapper.vm.closeModal();
    expect(wrapper.vm.showModal).toBe(false);
    expect(wrapper.vm.selectedFD).toEqual({});
  });

  it("copies FD ID to clipboard", async () => {
    await wrapper.vm.copyToClipboard("12345", "Copied!");
    expect(navigator.clipboard.writeText).toHaveBeenCalledWith("12345");
  });

  it("dispatches fetchFDs on mount if store is empty", () => {
    const emptyStore = {
      getters: { getFDs: [] },
      dispatch: vi.fn(),
      state: { fds: [], loading: false },
    };
    shallowMount(MyFDs, { global: { mocks: { $store: emptyStore } } });
    expect(emptyStore.dispatch).toHaveBeenCalledWith("fetchFDs");
  });

  it("handles FD broken successfully", async () => {
    store.dispatch.mockResolvedValue({ success: true, message: "FD broken successfully" });
    await wrapper.vm.handleFdBroken(sampleFDs[0]);
    await flushPromises();
    expect(store.dispatch).toHaveBeenCalledWith("breakFD", sampleFDs[0].id);
  });
});