import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { jobApi, applicationApi } from '../services/api';
import {
  Search,
  MapPin,
  Briefcase,
  DollarSign,
  CheckCircle,
  XCircle,
  Send,
  Sliders,
  Sparkles,
  Info,
  Clock,
  Layers,
  ChevronRight,
  TrendingUp,
} from 'lucide-react';

const Dashboard = () => {
  const { user } = useAuth();
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [location, setLocation] = useState('All');
  const [jobType, setJobType] = useState('All');
  const [minScore, setMinScore] = useState(0);
  const [selectedJob, setSelectedJob] = useState(null);
  const [applyLoading, setApplyLoading] = useState(null);
  const [applyMessage, setApplyMessage] = useState(null);

  const fetchJobs = async () => {
    setLoading(true);
    try {
      const res = await jobApi.getJobs({
        userId: user?.id,
        search,
        location,
        jobType,
        minScore,
      });
      if (res.data?.success) {
        setJobs(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load jobs', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchJobs();
  }, [user?.id, search, location, jobType, minScore]);

  const handleApply = async (jobId) => {
    if (!user) return;
    setApplyLoading(jobId);
    setApplyMessage(null);
    try {
      const res = await applicationApi.apply(user.id, jobId);
      if (res.data?.success) {
        setApplyMessage({ type: 'success', text: 'Application submitted successfully!' });
        // Update job in local state
        setJobs((prev) =>
          prev.map((j) =>
            j.id === jobId ? { ...j, hasApplied: true, applicationStatus: 'Applied' } : j
          )
        );
        if (selectedJob && selectedJob.id === jobId) {
          setSelectedJob((prev) => ({ ...prev, hasApplied: true, applicationStatus: 'Applied' }));
        }
      }
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to apply';
      setApplyMessage({ type: 'error', text: msg });
    } finally {
      setApplyLoading(null);
      setTimeout(() => setApplyMessage(null), 4000);
    }
  };

  const getScoreBadgeClass = (score) => {
    if (score >= 75) return 'badge-high';
    if (score >= 50) return 'badge-medium';
    return 'badge-low';
  };

  const userSkillCount = user?.skills?.length || 0;
  const highMatchJobs = jobs.filter((j) => j.matchScore >= 50).length;
  const appliedJobsCount = jobs.filter((j) => j.hasApplied).length;

  return (
    <div className="dashboard-container">
      {/* Top Banner */}
      <div className="hero-banner">
        <div className="hero-content">
          <h1>
            Smart Match Dashboard <Sparkles className="hero-icon" size={24} />
          </h1>
          <p>
            Jobs ranked automatically by your skills match score: 
            <strong> (Your Skills ∩ Job Skills / Required Skills) × 100%</strong>
          </p>
        </div>

        <div className="hero-stats">
          <div className="stat-card">
            <Layers className="stat-icon" size={22} />
            <div className="stat-info">
              <span className="stat-value">{userSkillCount}</span>
              <span className="stat-label">Your Skills</span>
            </div>
          </div>
          <div className="stat-card">
            <TrendingUp className="stat-icon text-success" size={22} />
            <div className="stat-info">
              <span className="stat-value">{highMatchJobs}</span>
              <span className="stat-label">≥ 50% Match</span>
            </div>
          </div>
          <div className="stat-card">
            <CheckCircle className="stat-icon text-primary" size={22} />
            <div className="stat-info">
              <span className="stat-value">{appliedJobsCount}</span>
              <span className="stat-label">Applied Jobs</span>
            </div>
          </div>
        </div>
      </div>

      {applyMessage && (
        <div className={`notification-toast ${applyMessage.type === 'success' ? 'toast-success' : 'toast-error'}`}>
          {applyMessage.type === 'success' ? <CheckCircle size={18} /> : <XCircle size={18} />}
          <span>{applyMessage.text}</span>
        </div>
      )}

      {/* Filter and Search Bar */}
      <div className="filter-card">
        <div className="search-input-wrapper">
          <Search size={18} className="search-icon" />
          <input
            type="text"
            placeholder="Search by title, company, or keyword (e.g., Java, React)..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className="filter-controls">
          <div className="filter-item">
            <label>Location</label>
            <select value={location} onChange={(e) => setLocation(e.target.value)}>
              <option value="All">All Locations</option>
              <option value="Bengaluru">Bengaluru</option>
              <option value="Remote">Remote</option>
              <option value="Pune">Pune</option>
              <option value="Hyderabad">Hyderabad</option>
              <option value="Mumbai">Mumbai</option>
              <option value="Noida">Noida</option>
              <option value="Chennai">Chennai</option>
              <option value="Gurugram">Gurugram</option>
            </select>
          </div>

          <div className="filter-item">
            <label>Job Type</label>
            <select value={jobType} onChange={(e) => setJobType(e.target.value)}>
              <option value="All">All Types</option>
              <option value="Full-Time">Full-Time</option>
              <option value="Remote">Remote</option>
              <option value="Internship">Internship</option>
              <option value="Part-Time">Part-Time</option>
            </select>
          </div>

          <div className="filter-item slider-item">
            <label>
              Min Match Score: <strong>{minScore}%</strong>
            </label>
            <input
              type="range"
              min="0"
              max="100"
              step="5"
              value={minScore}
              onChange={(e) => setMinScore(Number(e.target.value))}
            />
          </div>
        </div>
      </div>

      {/* Jobs Grid */}
      {loading ? (
        <div className="loading-state">
          <div className="spinner"></div>
          <p>Analyzing skills and calculating job matches...</p>
        </div>
      ) : jobs.length === 0 ? (
        <div className="empty-state">
          <Briefcase size={48} className="empty-icon" />
          <h3>No jobs found</h3>
          <p>Try clearing filters or search terms, or add more skills to your profile.</p>
        </div>
      ) : (
        <div className="jobs-grid">
          {jobs.map((job) => (
            <div key={job.id} className="job-card">
              <div className="job-card-header">
                <div>
                  <h3 className="job-title">{job.title}</h3>
                  <div className="job-company">{job.company}</div>
                </div>
                <div className={`match-score-badge ${getScoreBadgeClass(job.matchScore)}`}>
                  <span className="score-num">{job.matchScore}%</span>
                  <span className="score-txt">Match</span>
                </div>
              </div>

              <div className="job-meta">
                <span className="meta-pill">
                  <MapPin size={14} /> {job.location || 'Not Specified'}
                </span>
                <span className="meta-pill">
                  <Briefcase size={14} /> {job.jobType || 'Full-Time'}
                </span>
                {job.salaryRange && (
                  <span className="meta-pill meta-salary">
                    <DollarSign size={14} /> {job.salaryRange}
                  </span>
                )}
                {job.experienceReq && (
                  <span className="meta-pill">
                    <Clock size={14} /> {job.experienceReq}
                  </span>
                )}
              </div>

              <p className="job-desc">
                {job.description ? job.description.slice(0, 140) + '...' : 'No description provided.'}
              </p>

              {/* Skills Analysis */}
              <div className="skills-analysis">
                <div className="analysis-group">
                  <span className="analysis-label text-success">
                    <CheckCircle size={14} /> Matched ({job.matchedSkills?.length || 0})
                  </span>
                  <div className="skills-tags">
                    {job.matchedSkills && job.matchedSkills.length > 0 ? (
                      job.matchedSkills.map((sk, idx) => (
                        <span key={idx} className="skill-chip chip-matched">
                          {sk}
                        </span>
                      ))
                    ) : (
                      <span className="text-muted text-xs">No matching skills</span>
                    )}
                  </div>
                </div>

                {job.missingSkills && job.missingSkills.length > 0 && (
                  <div className="analysis-group">
                    <span className="analysis-label text-warning">
                      <XCircle size={14} /> Missing ({job.missingSkills.length})
                    </span>
                    <div className="skills-tags">
                      {job.missingSkills.slice(0, 4).map((sk, idx) => (
                        <span key={idx} className="skill-chip chip-missing">
                          {sk}
                        </span>
                      ))}
                      {job.missingSkills.length > 4 && (
                        <span className="skill-chip chip-more">
                          +{job.missingSkills.length - 4} more
                        </span>
                      )}
                    </div>
                  </div>
                )}
              </div>

              {/* Card Footer Actions */}
              <div className="job-card-footer">
                <button
                  type="button"
                  className="btn-link"
                  onClick={() => setSelectedJob(job)}
                >
                  <Info size={16} /> Details
                </button>

                {job.hasApplied ? (
                  <span className="badge-applied">
                    <CheckCircle size={16} /> Applied ({job.applicationStatus || 'Applied'})
                  </span>
                ) : (
                  <button
                    type="button"
                    className="btn-apply"
                    disabled={applyLoading === job.id}
                    onClick={() => handleApply(job.id)}
                  >
                    <Send size={16} />
                    <span>{applyLoading === job.id ? 'Applying...' : 'Apply Now'}</span>
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Job Details Modal */}
      {selectedJob && (
        <div className="modal-backdrop" onClick={() => setSelectedJob(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <div>
                <h2>{selectedJob.title}</h2>
                <div className="modal-company">{selectedJob.company}</div>
              </div>
              <button className="modal-close" onClick={() => setSelectedJob(null)}>
                ×
              </button>
            </div>

            <div className="modal-body">
              <div className="modal-score-banner">
                <div className={`match-score-badge ${getScoreBadgeClass(selectedJob.matchScore)}`}>
                  <span className="score-num">{selectedJob.matchScore}%</span>
                  <span className="score-txt">Skill Match</span>
                </div>
                <div>
                  <h4>Your Compatibility</h4>
                  <p>
                    You have <strong>{selectedJob.matchedSkills?.length || 0}</strong> of{' '}
                    <strong>{selectedJob.skills?.size || selectedJob.skills?.length || 0}</strong> required skills.
                  </p>
                </div>
              </div>

              <div className="modal-section">
                <h3>Job Overview</h3>
                <div className="job-meta">
                  <span className="meta-pill"><MapPin size={14} /> {selectedJob.location}</span>
                  <span className="meta-pill"><Briefcase size={14} /> {selectedJob.jobType}</span>
                  <span className="meta-pill"><DollarSign size={14} /> {selectedJob.salaryRange}</span>
                  <span className="meta-pill"><Clock size={14} /> {selectedJob.experienceReq}</span>
                </div>
              </div>

              <div className="modal-section">
                <h3>Description</h3>
                <p className="modal-desc">{selectedJob.description}</p>
              </div>

              <div className="modal-section">
                <h3>Required Skills & Matching</h3>
                <div className="modal-skills-split">
                  <div>
                    <h4 className="text-success">Skills You Have:</h4>
                    <div className="skills-tags">
                      {selectedJob.matchedSkills?.map((s, i) => (
                        <span key={i} className="skill-chip chip-matched">✓ {s}</span>
                      ))}
                      {(!selectedJob.matchedSkills || selectedJob.matchedSkills.length === 0) && (
                        <p className="text-muted text-sm">None of your listed skills match yet.</p>
                      )}
                    </div>
                  </div>

                  <div>
                    <h4 className="text-warning">Skills You Need to Learn/Add:</h4>
                    <div className="skills-tags">
                      {selectedJob.missingSkills?.map((s, i) => (
                        <span key={i} className="skill-chip chip-missing">✗ {s}</span>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="modal-footer">
              <button className="btn-secondary" onClick={() => setSelectedJob(null)}>
                Close
              </button>
              {selectedJob.hasApplied ? (
                <span className="badge-applied">
                  <CheckCircle size={16} /> Already Applied
                </span>
              ) : (
                <button
                  className="btn-primary"
                  onClick={() => handleApply(selectedJob.id)}
                  disabled={applyLoading === selectedJob.id}
                >
                  <Send size={16} /> Apply for this Job
                </button>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;
